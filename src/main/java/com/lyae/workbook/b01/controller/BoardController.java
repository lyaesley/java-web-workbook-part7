package com.lyae.workbook.b01.controller;

import com.lyae.workbook.b01.dto.BoardDTO;
import com.lyae.workbook.b01.dto.BoardListAllDTO;
import com.lyae.workbook.b01.dto.PageRequestDTO;
import com.lyae.workbook.b01.dto.PageResponseDTO;
import com.lyae.workbook.b01.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    @Value("${com.lyae.upload.path}")
    private String uploadPath;
    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }

    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPOST(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("board POST register...");

        if(bindingResult.hasErrors()) {
            log.info("has errors...");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("boardDTO", boardDTO);
            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("/modify")
    public String modify( PageRequestDTO pageRequestDTO,
            @Valid BoardDTO boardDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes){

        log.info("board modify post....." + boardDTO);

        if(bindingResult.hasErrors()) {
            log.info("has errors...");
            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?"+link;
        }

        boardService.modify(boardDTO);

        redirectAttributes.addFlashAttribute("reuslt", "modified");

        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    }

    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {

        Long bno = boardDTO.getBno();
        log.info("remove post..." + bno);

        boardService.remove(bno);

        //???????????? ??????????????????????????? ?????????????????? ???????????? ??????
        log.info(boardDTO.getFileNames());
        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0) {
            removeFiles(fileNames);
        }
        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";
    }

    private void removeFiles(List<String> files) {

        for (String fileName : files) {
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

            String resourceName = resource.getFilename();

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());

                resource.getFile().delete();

                //???????????? ???????????????
                if(contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);

                    thumbnailFile.delete();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }//end for
    }
}

