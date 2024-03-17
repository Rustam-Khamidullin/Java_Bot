package edu.java.domain;

import edu.java.dto.repository.Link;
import java.net.URI;
import java.util.List;

public interface LinkRepository {
    Link addOrGetExists(Long chatId, URI url);

    Link remove(Long chatId, URI url);

    List<Link> findAll();

    List<Link> findAll(long seconds);

    List<Link> findAll(Long chatId);

    Link find(Long chatId, URI url);

    void updateLinks(Long[] ids);
}
