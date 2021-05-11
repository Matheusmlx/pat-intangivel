import VueI18n from 'vue-i18n'

export default {
    SET_ROTULOS_PERSONALIZADOS: (state, camposPersonalizados) => {
        window.$i18n = new VueI18n({
            locale: 'camposPersonalizados',
            messages: {camposPersonalizados: camposPersonalizados}
        })
        localStorage.setItem('camposPersonalizados', JSON.stringify(camposPersonalizados))
        state.camposPersonalizados = camposPersonalizados
    }
}
