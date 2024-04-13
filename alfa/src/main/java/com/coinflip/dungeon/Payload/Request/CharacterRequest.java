package com.coinflip.dungeon.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CharacterRequest {

    @NotBlank
    @Size(max = 50)
    String name;

    @NotBlank
    String description;

    boolean is_public;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsPublic() {
        return is_public;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsPublic(boolean is_public) {
        this.is_public = is_public;
    }

}
