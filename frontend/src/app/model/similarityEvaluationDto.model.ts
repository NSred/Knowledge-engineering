import { SimilarityDto } from "./similarityDto.model";

export class SimilarityEvaluationDto{
    object : SimilarityDto = new SimilarityDto()
    evaluation : string = ''

    constructor(obj?: any) {
        if (obj) {
          this.object = obj.object;
          this.evaluation = obj.evaluation;
        }
      }
}