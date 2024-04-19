package com.benchmark.education.security;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.Book;
import com.benchmark.education.entity.LoginSession;
import com.benchmark.education.entity.SalesLedger;
import com.benchmark.education.exception.UnAuthorizedException;
import com.benchmark.education.repository.AccountRepository;
import com.benchmark.education.repository.BookRepository;
import com.benchmark.education.repository.LoginSessionRepository;
import com.benchmark.education.repository.SalesLedgerRegister;
import com.benchmark.education.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SecurityFiler1 extends OncePerRequestFilter {

    final int RELOGIN_STATUS = 450;


    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SalesLedgerRegister salesLedgerRegister;

    @Autowired
    LoginSessionRepository loginSessionRepository;

    @Autowired
    private ObjectMapper objectMapper;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractJwtToken(request);
        System.out.println(accessToken + " : " + SecurityFiler1.class);
        System.out.println( request.getRequestURL().toString());
        boolean invalidStudentSession = false;

        if((accessToken != null) && (this.jwtUtils.validateJwtToken(accessToken)) ){
            StringBuffer requestURL = request.getRequestURL();

            // Get the query string (if any)
            String queryString = request.getQueryString();

            // Combine the URL and query string if it exists
            if (queryString != null) {
                requestURL.append("?").append(queryString);
            }
            String completeURL = requestURL.toString();
        //    String accountType = (String) this.jwtUtils.getClaimFromJWTToken(accessToken, "account-type");
            String email = (String) this.jwtUtils.getUserNameFromJwtToken(accessToken);
            List<Account> account = this.accountRepository.findByEmail(email);
            if(account.size()>0){
                String accountType = account.get(0).getAccountType().name();
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(accountType));
                SimpleGrantedAuthority resourceAuthority = getProtectedResourceRight(email, accountType, completeURL, account.get(0).getIsVerified());
                authorities.add(resourceAuthority);
                System.out.println(SecurityFiler1.class +" \nline: 68");
                authorities.add(new SimpleGrantedAuthority("COMMON"));

                // filter out null values
                authorities = authorities.stream().filter(authority -> authority!=null).collect(Collectors.toList());
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null , authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);



                //for examining if an invalid student login session  SessionHash
                String sessionHash = request.getHeader("SessionHash");
                if(sessionHash== null){
                    sessionHash = "";
                }
                if(Account.AccountType.STUDENT.name().equals(account.get(0).getAccountType().name())){
                    invalidStudentSession = !this.isActiveSession(account.get(0).getId(),sessionHash);
                }

            }

        }

        // Add CORS headers to allow all origins, methods, and headers (for demonstration purposes)
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // to check if it is invalid student login
        if(invalidStudentSession){
            // Create the custom error response
            ResponseDto<String> errorResponse = ResponseDto.Failure("","You are trying to" +
                    " access protected resource");

            // Convert the response to JSON
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

            // Set the response headers and write the JSON response
            response.setStatus(RELOGIN_STATUS);
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);
        }else{
            filterChain.doFilter(request, response);
        }

    }

    private String extractJwtToken(HttpServletRequest request) {
        // Extract the token from the Authorization header or any other header/parameter where it is sent
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private SimpleGrantedAuthority getProtectedResourceRight(String email,String accountType, String url, Boolean isVerified){
        if( Account.AccountType.ADMIN.name().equals(accountType)){
            System.out.println(SecurityFiler1.class +" \nline: 97");
            return new SimpleGrantedAuthority("PROTECTED_RESOURCE");
        } if(Account.AccountType.TEACHER.name().equals(accountType)){
            if(isVerified!=null && isVerified.booleanValue()==true){
                return new SimpleGrantedAuthority("PROTECTED_RESOURCE");
            }
        }
        else
            if(Account.AccountType.STUDENT.name().equals(accountType)){
                String fileName = url.substring(url.lastIndexOf("/")+1);
                List<Book> bookList = this.bookRepository.findByFileLocation("/files/protected/"+ fileName);
                if(bookList.size()==0){
                    return null;
                }

                Book book = bookList.get(0);
                List<SalesLedger> salesLedgerList = this.salesLedgerRegister.findByStudentEmailAndSubjectId(email, book.getSubjectId());
                if(salesLedgerList.size()==0){
                    return null;
                }

               return new SimpleGrantedAuthority("PROTECTED_RESOURCE");
            }

            return null;
    }

    boolean isActiveSession(int id, String sessionHash){
        Optional<LoginSession> loginSessionOptional = this.loginSessionRepository.findById(id);
        if(loginSessionOptional.isEmpty()){
            return true;
        }
        LoginSession loginSession = loginSessionOptional.get();
        if(loginSession.getSessionHash().equals(sessionHash)){
            return true;
        }

        return false;

    }
}
