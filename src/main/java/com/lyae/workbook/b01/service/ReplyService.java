package com.lyae.workbook.b01.service;

import com.lyae.workbook.b01.dto.PageRequestDTO;
import com.lyae.workbook.b01.dto.PageResponseDTO;
import com.lyae.workbook.b01.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
