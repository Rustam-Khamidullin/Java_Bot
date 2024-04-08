package edu.java.service;

import edu.java.dto.repository.Link;
import java.sql.Timestamp;
import java.util.List;

public interface LinkUpdateCheckerService {
    List<Link> getUnupdatedLinks(Timestamp before);

    void updateAllLinks(List<Link> links);
}
