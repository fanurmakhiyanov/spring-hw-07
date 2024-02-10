package ru.gb.springdemo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.gb.springdemo.model.dto.IssueRequest;
import ru.gb.springdemo.model.Issue;
import ru.gb.springdemo.repository.BookRepository;
import ru.gb.springdemo.repository.IssueRepository;
import ru.gb.springdemo.repository.ReaderRepository;
import ru.gb.springdemo.util.IssueRejectedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class IssuerService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final IssueRepository issueRepository;

    private long maxCountBooks;

    @Autowired
    public void setMaxCountBooks(@Value("${application.max-allowed-books:1}") long maxCountBooks) {
        this.maxCountBooks = maxCountBooks;
    }

    public Issue issue(IssueRequest request) throws IssueRejectedException {
        validateRequest(request);
        Issue issue = new Issue(request.getBookId(), request.getReaderId());
        issueRepository.save(issue);
        return issue;
    }

    public Issue getIssueById(long issueId) {
        return issueRepository.findById(issueId).orElseThrow(NoSuchElementException::new);
    }

    public void returnedBook(long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(NoSuchElementException::new);
        issue.setReturned(LocalDateTime.now());
        issueRepository.save(issue);
    }

    public List<Issue> getIssues() {
        return issueRepository.findAll();
    }

    private void validateRequest(IssueRequest request) throws IssueRejectedException {
        bookRepository.findById(request.getBookId()).orElseThrow(NoSuchElementException::new);
        readerRepository.findById(request.getReaderId()).orElseThrow(NoSuchElementException::new);
        if (!bookIsAcceptably(request.getBookId())) {
            throw new IssueRejectedException("Нет свободной книги");
        }
        if (!readerCanTakeBook(request.getReaderId())) {
            throw new IssueRejectedException("Читателю отказанно в получение книги");
        }
    }

    private boolean bookIsAcceptably(long bookId) {
        List<Issue> issueList = issueRepository.findAll();
        if (issueList.size() == 0) {return true;}
        return issueList.stream().noneMatch(x -> x.getBookId() == bookId);
    }

    private boolean readerCanTakeBook(long readerId) {
        List<Issue> issueList = issueRepository.findByReturned(null);
        if (issueList.size() == 0) {return true;}
        return issueList.stream().filter(x -> x.getReaderId() == readerId).count() < maxCountBooks;
    }
}