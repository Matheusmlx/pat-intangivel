import textoSemValor from './texto-sem-valor'

export default {
    install(Vue) {
        Vue.filter('textoSemValor', textoSemValor)
    }
}
