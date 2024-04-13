package com.coinflip.dungeon.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CampaignRequest {

    @NotBlank
    @Size(max = 50)
    String name;

    @NotBlank
    String description;

    String cover_image;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoverImage(String cover_image) {
        this.cover_image = cover_image;
    }
}
