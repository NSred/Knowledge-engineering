export class GroupMembershipDto{
    bad : number = 0;
    average : number = 0;
    excellent : number = 0;

    public constructor(obj?: any){
        if(obj){
            this.bad = obj.bad
            this.average = obj.average
            this.excellent = obj.excellent
        }
    }
}