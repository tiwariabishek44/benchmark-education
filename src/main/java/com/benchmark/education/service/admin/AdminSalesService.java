package com.benchmark.education.service.admin;

import com.benchmark.education.dto.Reponse.AccountResponseDto;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Reponse.TeacherManualResponsee;
import com.benchmark.education.dto.Reponse.TeacherSubjectResponse;
import com.benchmark.education.entity.*;
import com.benchmark.education.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminSalesService {



    @Autowired
    private EcommerceInquiryRepository ecommerceInquiryRepository;

    @Autowired
    private SalesLedgerRegister salesLedgerRegister;

    @Autowired
    ManualRepository manualRepository;

    @Autowired
    ManualLedgerRepository manualLedgerRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    private AccountRepository accountRepository;


    public ResponseDto<List<AccountResponseDto>> getStudentsWithPurchase(){
        List<SalesLedger>  salesLedgerList = this.salesLedgerRegister.findAll();
        List<String> studentEmails = salesLedgerList.stream().map(salesLedger -> salesLedger.getEmail()).filter(s -> s!=null).collect(Collectors.toList());
        List<Account> studentAccounts = this.accountRepository.findByAccountTypeAndEmailIn(Account.AccountType.STUDENT,studentEmails);
        List< AccountResponseDto> accountResponseDtoList = studentAccounts.stream().map(studentAccount -> {
           AccountResponseDto accountResponseDto = AccountResponseDto.builder()
                   .accountType(Account.AccountType.STUDENT)
                   .name(studentAccount.getName())
                   .email(studentAccount.getEmail())
                   .phoneNumber(studentAccount.getPhoneNumber())
                   .password("")
                   .isActive(studentAccount.getIsActive())
                   .isVerified(studentAccount.getIsVerified())
                   .createdDate(studentAccount.getCreatedDate())
                   .stream(studentAccount.getStream())
                   .build();
           return accountResponseDto;
        }).toList();
        return ResponseDto.Success(accountResponseDtoList, null);
    }

    public ResponseDto<List<EcomerceEnquiry>> getAllEnquiry(){
        List<EcomerceEnquiry> ecomerceEnquiryList = this.ecommerceInquiryRepository.findAll();
        return ResponseDto.Success(ecomerceEnquiryList, null);
    }

    public ResponseDto<String> deleteEcommerceInquiry(int id){
        this.ecommerceInquiryRepository.deleteById(id);
        return ResponseDto.Success("", null);
    }

//    public  List<TeacherManualResponsee> getManualAssignedToTeacher(String email){
//        List<ManualLedger> manualLedgerList = this.manualLedgerRepository.findByEmail(email);
//        List<Integer> manualIdList = manualLedgerList.stream().map(manualLedger -> manualLedger.getManualId())
//                .collect(Collectors.toList());
//        List<Manual> manualList = this.manualRepository.findAllById(manualIdList);
//        List<TeacherManualResponsee> teacherManualResponseeList= manualList.stream().map(manual -> {
//            TeacherManualResponsee manualResponsee = new TeacherManualResponsee();
//            manualResponsee.setManual(manual);
//            manualResponsee.setAssigned(true);
//            return manualResponsee;
//        }).collect(Collectors.toList());
//
//        List<Manual> notAssignedManualList = this.manualRepository.findByIdNotIn(manualIdList);
//
//        teacherManualResponseeList.addAll(notAssignedManualList.stream().map(manual -> {
//            TeacherManualResponsee manualResponsee = new TeacherManualResponsee();
//            manualResponsee.setManual(manual);
//            manualResponsee.setAssigned(false);
//            return manualResponsee;
//        }).collect(Collectors.toList()));
//
//        return teacherManualResponseeList;
//    }
//
//
//
//
//    public ResponseDto<String> addManualToTeacher(String teacherEmail, int manualId){
//        List<ManualLedger> manualLedgerList = this.manualLedgerRepository.findByEmailAndManualId(teacherEmail, manualId);
//        if(manualLedgerList.isEmpty()){
//            ManualLedger manualLedger = new ManualLedger();
//            manualLedger.setEmail(teacherEmail);
//            manualLedger.setManualId(manualId);
//            this.manualLedgerRepository.save(manualLedger);
//        }
//
//        return ResponseDto.Success("", "Successfully assigned");
//    }
//
//    public ResponseDto<String> unAssignManualToTeacher(int id){
//     this.manualLedgerRepository.deleteById(id);
//        return ResponseDto.Success("", "Successfully unassigned");
//    }

    public ResponseDto<String> assignSubjectToTeacher(String teacherEmail, int subjectId){
     List<SalesLedger> salesLedgerList = this.salesLedgerRegister.findByEmailAndSubjectId(teacherEmail,subjectId
             );

     if(salesLedgerList.isEmpty()){
         SalesLedger salesLedger = new SalesLedger();
         salesLedger.setSubjectId(subjectId);
         salesLedger.setEmail(teacherEmail);
         this.salesLedgerRegister.save(salesLedger);
     }

        return ResponseDto.Success("", "Successfully assigned");
    }

    public ResponseDto<String> unAssignSubjectToTeacher(int subjectId, String email){
        this.salesLedgerRegister.deleteBySubjectIdAndEmail(subjectId,email);
        return ResponseDto.Success("", "Successfully unassigned");
    }

    public  List<TeacherSubjectResponse> getSubjectAssignedToTeacher(String email){

        List<SalesLedger> salesLedgerList = this.salesLedgerRegister.findByEmail(email);
        List<Integer> subjectIdList = salesLedgerList.stream().map(salesLedger -> salesLedger.getSubjectId())
                .collect(Collectors.toList());
        List<Subject> subjectList = this.subjectRepository.findAll();
        List<Subject> assignedSubjects = subjectList.stream().filter(subject ->
                subjectIdList.contains(subject.getId())).collect(Collectors.toList());

        List<Subject> unAssignedSubjects = subjectList.stream().filter(subject ->
                !subjectIdList.contains(subject.getId())).collect(Collectors.toList());
        List<TeacherSubjectResponse> teacherSubjectResponseeList= assignedSubjects.stream().map(manual -> {
            TeacherSubjectResponse manualResponsee = new TeacherSubjectResponse();
            manualResponsee.setSubject(manual);
            manualResponsee.setAssigned(true);
            return manualResponsee;
        }).collect(Collectors.toList());

        teacherSubjectResponseeList.addAll(unAssignedSubjects.stream().map(manual -> {
            TeacherSubjectResponse manualResponsee = new TeacherSubjectResponse();
            manualResponsee.setSubject(manual);
            manualResponsee.setAssigned(false);
            return manualResponsee;
        }).collect(Collectors.toList()));

        return teacherSubjectResponseeList;
    }


}
