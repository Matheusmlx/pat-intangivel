import camposPersonalizadosDefault from './camposPersonalizadosDefault'
import VueI18n from 'vue-i18n'

const camposPersonalizados = localStorage.getItem('camposPersonalizados') && JSON.parse(localStorage.getItem('camposPersonalizados')) || camposPersonalizadosDefault;
window.$i18n = new VueI18n({locale: 'camposPersonalizados', messages: {camposPersonalizados}})

export default {
    camposPersonalizados
}
