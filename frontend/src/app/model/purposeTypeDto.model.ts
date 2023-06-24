import { GroupMembershipDto } from "./groupMembershipDto.model";

export class PurposeTypeDto{
     appDevelopment : GroupMembershipDto = new GroupMembershipDto()
     videoGames : GroupMembershipDto = new GroupMembershipDto()
     mining : GroupMembershipDto = new GroupMembershipDto()
     home : GroupMembershipDto = new GroupMembershipDto()
     business : GroupMembershipDto = new GroupMembershipDto()
     hosting : GroupMembershipDto = new GroupMembershipDto()

     public constructor(obj?: any){
        if(obj){
            this.appDevelopment = obj.appDevelopment
            this.videoGames = obj.videoGames
            this.mining = obj.mining
            this.home = obj.home
            this.business = obj.business
            this.hosting = obj.hosting
        }
    }
}