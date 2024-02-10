package ru.gb.springdemo.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.springdemo.model.dto.IssueResponseDto;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.service.BookService;
import ru.gb.springdemo.service.IssuerService;
import ru.gb.springdemo.service.ReaderService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
@Tag(name = "UI")
public class UiController {

    private final BookService bookService;
    private final ReaderService readerService;
    private final IssuerService issuerService;

    @GetMapping("/books")
    public String available(Model model) {
        model.addAttribute("books",bookService.getAllAccessibleBooks());
        return "available";
    }


    @GetMapping("/readers")
    public String readers(Model model) {
        model.addAttribute("readers", readerService.getAllReaders());
        return "readers";
    }


    @GetMapping("/readers/{id}")
    public String readers(@PathVariable long id, Model model) {
        model.addAttribute("reader", readerService.getReaderById(id));
        model.addAttribute("books", readerService.getAllBooksReader(id));
        return "reader_books";
    }


    @GetMapping("/issues")
    public String issues (Model model) {
        List<IssueResponseDto> issueResponseDtoList = new ArrayList<>();
        for (Issue issue : issuerService.getIssues()) {
            issueResponseDtoList.add( new IssueResponseDto(
                    bookService.getBookById(issue.getBookId()).getTitle(),
                    readerService.getReaderById(issue.getReaderId()).getName(),
                    issue.getReceived(),
                    issue.getReturned()
            ));
        }
        model.addAttribute("rows", issueResponseDtoList);
        return "table";
    }
}