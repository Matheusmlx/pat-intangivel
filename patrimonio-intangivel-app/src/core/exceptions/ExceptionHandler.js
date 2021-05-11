import router from '@/views/routers'
import store from '@/core/store'
import {Alert, PageUtils} from '@/core/utils'
import ApiErrorValidations from '@/core/exceptions/ApiErrorValidations'
import {mensagens, mutationTypes} from '@/core/constants'

const alert = new Alert(store)
const pageUtils = new PageUtils(router)

class ExceptionHandler {
    execute(error) {
        if (error instanceof ApiErrorValidations) {
            this.tratarValidationError(error)
        } else {
            this.tratarError(error)
        }
        store.commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        store.commit(mutationTypes.LOKI.SET_GLOBAL_LOADING, false)
        store.commit(mutationTypes.LOKI.SET_LOADING_MESSAGE, store.state.defaultLoadingMessage)
    }

    tratarValidationError(error) {
        if (error.multipleErrors()) {
            this.handleMultipleErrors(error.response.data.errorMessages)
        } else if (error.disconnected()) {
            this.handleDisconnected()
        } else if (error.unauthorized()) {
            this.handleUnauthorized()
        } else if (error.internalError()) {
            this.handleInternalError(error)
        } else if (error.notFound()) {
            this.handleNotFound(error)
        } else {
            this.handleUnknown()
        }
    }

    tratarError(error) {
        alert.showError(error.message)
    }

    handleMultipleErrors(errors) {
        const errorMsg = errors.map(err => err.message).reduce((total, prox) => {
            return total + ' \n' + prox
        })
        alert.showError(errorMsg)
    }

    handleDisconnected() {
        alert.showError(mensagens.DISCONNECTED)
        pageUtils.goToHome()
    }

    handleUnauthorized() {
        alert.showError(mensagens.LOST_SESSION)
        pageUtils.reload()
    }

    handleNotFound(error) {
        this.handleInternalError(error)
        pageUtils.goToHome()
    }

    handleInternalError(error) {
        if(typeof error.response.data.message !== 'undefined'){
            const msgFormatted = error.response.data.message
            if (msgFormatted) {
                alert.showError(msgFormatted)
            } else {
                this.handleUnknown()
            }
        }else{
            var dataView = new DataView(error.response.data)
            var decoder = new TextDecoder('utf8')
            var response = JSON.parse(decoder.decode(dataView))
            var message = response['message']
            alert.showError(message)
        }
    }

    handleUnknown() {
        alert.showError(mensagens.UNKNOWN)
    }
}

export default new ExceptionHandler()
