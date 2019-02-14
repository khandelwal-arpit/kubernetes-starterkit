import Api from '@/services/Api'

export default {
    getOperations () {
        return Api('bootstorage').get('/operations');
    }
}