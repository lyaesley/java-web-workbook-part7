package com.lyae.workbook.b01.service;


import com.lyae.workbook.b01.dto.BoardDTO;
import com.lyae.workbook.b01.dto.PageRequestDTO;
import com.lyae.workbook.b01.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test void testRegister() {

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info(bno);
    }

    @Test void testReadOne() {
        BoardDTO boardDTO = boardService.readOne(101L);

        log.info(boardDTO);
    }

    @Test void testModify() {

        //변경에 필요한 데이터만
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Updated...101")
                .content("Updated Content...101")
                .build();

        boardService.modify(boardDTO);
    }

    @Test void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }
}
