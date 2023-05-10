package com.app.dodamdodam.repository;

import com.app.dodamdodam.entity.free.FreeBoard;
import com.app.dodamdodam.entity.free.FreeFile;
import com.app.dodamdodam.entity.purchase.Product;
import com.app.dodamdodam.entity.purchase.PurchaseBoard;
import com.app.dodamdodam.entity.recruitment.Recruitment;
import com.app.dodamdodam.entity.recruitment.RecruitmentBoard;
import com.app.dodamdodam.repository.board.free.FreeBoardRepository;
import com.app.dodamdodam.repository.board.purchase.PurchaseBoardRepository;
import com.app.dodamdodam.repository.board.recruitment.RecruitmentBoardRepository;
import com.app.dodamdodam.repository.file.freeFile.FreeFileRepository;
import com.app.dodamdodam.repository.member.MemberRepository;
import com.app.dodamdodam.repository.recruitment.RecruitmentRepository;
import com.app.dodamdodam.type.CategoryType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Rollback(false)
@Transactional
@Slf4j
public class BoardRepositoryTests {

    @Autowired
    private RecruitmentBoardRepository recruitmentBoardRepository;

    @Autowired
    private FreeBoardRepository freeBoardRepository;

    @Autowired
    private PurchaseBoardRepository purchaseBoardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private FreeFileRepository freeFileRepository;

    ArrayList<CategoryType> categoryTypes = new ArrayList<CategoryType>(Arrays.asList(CategoryType.ALL, CategoryType.CULTURE, CategoryType.DAILY, CategoryType.EVENT, CategoryType.PURCHASE, CategoryType.RECRUITMENT));

    /*모집 게시글 등록*/
    @Test
    public void saveTest(){
//        for (int i=1; i<=100; i++){
//            RecruitmentBoard recruitmentBoard = new RecruitmentBoard("모집 게시글 제목" + i, LocalDate.now(),10 + i, "www.naver.com", "1234", "경기도 성남시", "분당구 수내동");
//            memberRepository.findById(2L).ifPresent(member -> recruitmentBoard.setMember(member));
//            recruitmentBoardRepository.save(recruitmentBoard);
//        }
        RecruitmentBoard recruitmentBoard = new RecruitmentBoard("모집 게시글 제목", LocalDate.now(),10, "www.naver.com", "1234", "서울특별시", "강남구");
        memberRepository.findById(5L).ifPresent(member -> recruitmentBoard.setMember(member));
//        recruitmentBoard.addRecruitment();
        recruitmentBoardRepository.save(recruitmentBoard);
    }

    /*자유 게시글 등록*/
    @Test
    public void saveTest2(){
//        FreeBoard freeBoard = new FreeBoard("자유 게시글 제목","자유 게시글 내용" , categoryTypes.get(1), );
//        memberRepository.findById(5L).ifPresent(member -> freeBoard.setMember(member));
//        freeBoardRepository.save(freeBoard);
//        for (int i=1; i<=100; i++){
//            FreeBoard freeBoard = new FreeBoard("자유 게시글 제목" + i,"자유 게시글 내용" + i, categoryTypes.get(i % 6));
//            memberRepository.findById(2L).ifPresent(member -> freeBoard.setMember(member));
//            freeBoardRepository.save(freeBoard);
//        }
    }

    /*판매 게시글 등록*/
    @Test
    public void saveTest3(){
        for (int i=1; i<=100; i++){
            Product product = new Product("상품" + i, 1000 * i, (long)i);
            PurchaseBoard purchaseBoard = new PurchaseBoard("판매 게시글 제목" + i, "판매 게시글 내용"+ i);

            memberRepository.findById(2L).ifPresent(member -> {
                purchaseBoard.setMember(member);
                purchaseBoard.setProduct(product);
            });

            purchaseBoardRepository.save(purchaseBoard);
        }
    }

    /* id로 내가 작성한 자유게시글 목록 가져오기*/
    @Test
    public void findByIdTest(){
        Pageable pageable = PageRequest.of(0,10);
        freeBoardRepository.findFreeBoardListByMemberId(pageable, 5L).stream().map(FreeBoard::toString).forEach(log::info);
    }

    /* 모집게시글에 참여하기 테스트*/
    /* 200번 모집 게시글에 임의로 5번 유저 참석 시켰음*/
    @Test
    public void saveTest4(){
        memberRepository.findById(5L).ifPresent(member ->
        {
            Recruitment recruitment = new Recruitment(member);
            recruitmentBoardRepository.findById(403L).ifPresent(recruitmentBoard -> recruitment.setRecruitmentBoard(recruitmentBoard));
            recruitmentRepository.save(recruitment);
        });
    }

    /* 자유 게시글 작성 */
    @Test
    public void saveFreeBoardTest(){
        FreeBoard freeBoard = new FreeBoard("자유게시글 제목", "자유게시글 내용", categoryTypes.get(1));
        memberRepository.findById(5L).ifPresent(member -> freeBoard.setMember(member));
        freeBoardRepository.save(freeBoard);

        List<FreeFile> freeFiles = new ArrayList<FreeFile>();
        FreeFile freeFile = new FreeFile("fileName1", UUID.randomUUID().toString(),"filePath1",500L);
        FreeFile freeFile2 = new FreeFile("fileName2", UUID.randomUUID().toString(),"filePath2",500L);
        freeFile.setFreeBoard(freeBoard);
        freeFile2.setFreeBoard(freeBoard);

        freeFiles.add(freeFile);
        freeFiles.add(freeFile2);
        log.info(freeFile.toString());
        log.info(freeFile2.toString());
        log.info(freeFiles.toString());
        freeFileRepository.saveAll(freeFiles);

//        for (int i=1; i<=5; i++) {
//            List<FreeFile> freeFiles = new ArrayList<FreeFile>();
//            FreeFile freeFile = new FreeFile("fileName1", UUID.randomUUID().toString(),"filePath" + i,500L);
//            FreeFile freeFile2 = new FreeFile("fileName2", UUID.randomUUID().toString(),"filePath" + i,500L);
//            freeBoardRepository.findById()
//            freeFiles.add(freeFile);
//            freeFiles.add(freeFile2);
//            log.info(freeFile.toString());
//            log.info(freeFiles.toString());
//            freeFileRepository.saveAll(freeFiles);
//            FreeBoard freeBoard = new FreeBoard("자유게시글 제목" + i, "자유게시글 내용" + i, categoryTypes.get(i));
//            freeBoard.setFreeFiles(freeFileRepository.findAll());
//            memberRepository.findById(5L).ifPresent(member -> freeBoard.setMember(member));
//            freeBoardRepository.save(freeBoard);
//            /*file이 안나옴*/
//        }
    }

    /* 자유 게시글 목록 */
    @Test
    public void findAllFreeBoardListTest(){
        Pageable pageable = PageRequest.of(0, 10);
        freeBoardRepository.findAllFreeBoardList(pageable).stream().map(FreeBoard::toString).forEach(log::info);
    }

    /* 자유 게시글 목록 분류 */
    @Test
    public void findFreeBoardListByCategoryTypeTest(){
        Pageable pageable = PageRequest.of(0, 10);
        freeBoardRepository.findFreeBoardListByCategoryType(pageable,CategoryType.CULTURE).stream().map(FreeBoard::toString).forEach(log::info);
    }

    /* 내가 작성한 자유 게시글 목록 분류 */
    @Test
    public void findFreeBoardListByCategoryTypeAndMemberIdTest(){
        Pageable pageable = PageRequest.of(0, 10);
        freeBoardRepository.findFreeBoardListByCategoryTypeAndMemberId(pageable,CategoryType.CULTURE, 5L).stream().map(FreeBoard::toString).forEach(log::info);
    }

    /* 자유 게시글 상세 */
    /* 자유 게시글 수정 */
    /* 자유 게시글 삭제 */

}