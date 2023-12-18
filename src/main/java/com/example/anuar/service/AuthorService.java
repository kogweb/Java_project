package com.example.anuar.service;

import com.example.anuar.dto.AuthorDTO;
import com.example.anuar.entity.Author;
import com.example.anuar.entity.Genre;
import com.example.anuar.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author updateAuthor(Long authorId, Author updatedAuthor) {
        Author author = getAuthorById(authorId);
        author.setName(updatedAuthor.getName());
        // Добавьте другие поля, если необходимо
        return authorRepository.save(author);
    }

    public void deleteAuthor(Long authorId) {
        Author author = getAuthorById(authorId);
        authorRepository.delete(author);
    }

    public List<AuthorDTO> getAllAuthors(int page, int size, String sortBy, String order) {
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Author> all = authorRepository.findAll(pageable);
        return all.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();

        authorDTO.setInformation(author.getInformation());
        authorDTO.setName(author.getName());
        return authorDTO;
        }

    public Author getAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + authorId));
    }
}
