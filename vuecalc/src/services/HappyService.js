import Api from '@/services/Api'

export default {
    getMultiplicationResult (num1, num2) {
        return Api('happy').get('/multiply?num1=' + num1 + '&num2=' + num2);
    },
    getDivisionResult (num1, num2) {
        return Api('happy').get('/divide?num1=' + num1 + '&num2=' + num2);
    }
}