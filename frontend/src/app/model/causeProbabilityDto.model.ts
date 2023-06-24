export class CauseProbabilityDto{
    cause : string = '';
    probability : number = 0;

    public constructor(obj?: any){
        if(obj){
            this.cause = obj.cause
            this.probability = obj.probability
        }
    }
}