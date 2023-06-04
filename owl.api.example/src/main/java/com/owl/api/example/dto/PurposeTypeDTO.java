package com.owl.api.example.dto;

import lombok.Data;

@Data
public class PurposeTypeDTO {
    private GroupMembershipDTO appDevelopment;
    private GroupMembershipDTO videoGames;
    private GroupMembershipDTO mining;
    private GroupMembershipDTO home;
    private GroupMembershipDTO business;
    private GroupMembershipDTO hosting;
}
