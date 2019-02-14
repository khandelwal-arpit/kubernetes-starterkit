import Api from '@/services/Api'

export default {
    getAdditionResult (num1, num2) {
        return Api('expressed').get('/add?num1=' + num1 + '&num2=' + num2);
    },
    getSubtractionResult (num1, num2) {
        return Api('expressed').get('/subtract?num1=' + num1 + '&num2=' + num2);
    }
}