package taskman.Backend.resource;

import java.time.LocalDateTime;

public class ResourceItem implements Resource {

    @Override
    public boolean isAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        // TODO
        return false;
    }

    @Override
    public boolean isAvailable(LocalDateTime time) {
        // TODO
        return false;
    }
}
