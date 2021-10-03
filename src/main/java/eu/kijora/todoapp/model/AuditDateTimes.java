package eu.kijora.todoapp.model;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

//@MappedSuperclass - drawbacks of constant inheritance... what if also BaseIdClass would be needed etc.
@Embeddable //to be embedded just in the class - composition
class AuditDateTimes {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
    void prePersist() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedOn = LocalDateTime.now();
    }


}
