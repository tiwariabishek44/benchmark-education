package com.benchmark.education.service.admin;

import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Reponse.AccountResponseDto;
import com.benchmark.education.entity.Account;
import com.benchmark.education.exception.NoSuchAccountException;
import com.benchmark.education.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class AdminService {

    @Autowired
    private AccountRepository accountRepository;


    public ResponseDto<String> verifyTeacher(int id) {
        Optional<Account> accountOptional = this.accountRepository.findById(id);

        if(accountOptional.isEmpty()){
            throw new NoSuchAccountException("Invalid Teacher Account Provided");
        }
        Account account = accountOptional.get();

        account.setIsVerified(true);
        this.accountRepository.save(account);

        return ResponseDto.Success("","");
    }

    public ResponseDto<List<AccountResponseDto>> getUnverifiedTeacher(){
        List<Account> accountList = this.accountRepository.findByAccountTypeAndIsVerified(Account.AccountType.TEACHER,false);
        List<AccountResponseDto> accountList1 = accountList.stream().map(account -> {
            AccountResponseDto dto = AccountResponseDto.builder()
                    .id(account.getId())
                    .email(account.getEmail())
                    .password("")
                    .name(account.getName())
                    .accountType(account.getAccountType())
                    .isVerified(account.getIsVerified())
                    .isActive(account.getIsActive())
                    .phoneNumber(account.getPhoneNumber())
                    .stream(account.getStream())
                    .createdDate(account.getCreatedDate())
                    .build();
            return dto;
        }).collect(Collectors.toList());
        return ResponseDto.Success(accountList1,"");
    }

    public ResponseDto<List<AccountResponseDto>> getVerifiedTeacher(){
        List<Account> accountList = this.accountRepository.findByAccountTypeAndIsVerified(Account.AccountType.TEACHER,true);
        List<AccountResponseDto> accountList1 = accountList.stream().map(account -> {
            AccountResponseDto dto = AccountResponseDto.builder().email(account.getEmail())
                    .id(account.getId())
                    .password("")
                    .name(account.getName())
                    .accountType(account.getAccountType())
                    .isVerified(account.getIsVerified())
                    .isActive(account.getIsActive())
                    .phoneNumber(account.getPhoneNumber())
                    .stream(account.getStream())
                    .createdDate(account.getCreatedDate())
                    .build();
            return dto;
        }).collect(Collectors.toList());
        return ResponseDto.Success(accountList1,"");
    }

}

