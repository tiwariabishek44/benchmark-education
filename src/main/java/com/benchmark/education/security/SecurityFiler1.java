package com.benchmark.education.security;

import com.benchmark.education.entity.Account;
import com.benchmark.education.entity.Book;
import com.benchmark.education.entity.SalesLedger;
import com.benchmark.education.exception.UnAuthorizedException;
import com.benchmark.education.repository.AccountRepository;
import com.benchmark.education.repository.BookRepository;
import com.benchmark.education.repository.SalesLedgerRegister;
import com.benchmark.education.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class SecurityFiler1 extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SalesLedgerRegister salesLedgerRegister;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractJwtToken(request);

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
                SimpleGrantedAuthority resourceAuthority = getProtectedResourceRight(email, accountType, completeURL);
                authorities.add(resourceAuthority);
                System.out.println(SecurityFiler1.class +" \nline: 68");
                authorities.add(new SimpleGrantedAuthority("COMMON"));
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null , authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

        // Add CORS headers to allow all origins, methods, and headers (for demonstration purposes)
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");



        filterChain.doFilter(request, response);
    }

    private String extractJwtToken(HttpServletRequest request) {
        // Extract the token from the Authorization header or any other header/parameter where it is sent
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private SimpleGrantedAuthority getProtectedResourceRight(String email,String accountType, String url){
        if(Account.AccountType.TEACHER.name().equals(accountType)|| Account.AccountType.ADMIN.name().equals(accountType)){
            System.out.println(SecurityFiler1.class +" \nline: 97");
            return new SimpleGrantedAuthority("PROTECTED_RESOURCE");
        }else
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
}
