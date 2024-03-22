package edu.java.domain.jpa;

import edu.java.domain.jpa.entity.ChatEntity;
import edu.java.domain.jpa.entity.LinkEntity;
import java.net.URI;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {
    default LinkEntity saveIfNotExists(Long tgChatId, URI url) {
        var chat = new ChatEntity();
        chat.setId(tgChatId);

        return findByChatAndUrl(chat, url.toString()).orElseGet(() -> {
            LinkEntity newLink = new LinkEntity();
            newLink.setUrl(url.toString());
            newLink.setChat(chat);
            return save(newLink);
        });
    }

    @EntityGraph(attributePaths = "chat")
    Optional<LinkEntity> findByChatAndUrl(ChatEntity chat, String url);

    void deleteByChatAndUrl(ChatEntity chat, String url);

    @EntityGraph(attributePaths = "chat")
    List<LinkEntity> findAllByChat(ChatEntity chat);

    @EntityGraph(attributePaths = "chat")
    List<LinkEntity> findByLastUpdateBefore(Timestamp timestamp);

    @Modifying
    @Query("UPDATE LinkEntity l SET l.lastUpdate = CURRENT_TIMESTAMP WHERE l.id IN :ids")
    void updateLastUpdateForIds(@Param("ids") Set<Long> ids);

}
