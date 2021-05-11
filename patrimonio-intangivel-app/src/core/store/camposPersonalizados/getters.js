import _ from 'lodash'

export default {
    getCamposPersonalizados: () => window.$i18n.getLocaleMessage(window.$i18n.locale),

    getNomeCamposPersonalizados: () => (campoNome, telaNome) => {
        return (window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome] || {}).nome
    },

    getObrigatorioCamposPersonalizados: () => (campoNome, telaNome) => {
        return JSON.parse((window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome] || {}).obrigatorio)
    },

    getTooltipCamposPersonalizados: () => (campoNome, telaNome) => {
        return (window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome] || {}).tooltip
    },

    getObjetoValidado: () => (objetoEntidade, telaNome, permissaoRetroativa, permissaoRetroativaExpirada) => {
        if (!window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome]) return false
        let valido = true
        let geracaoOuTransacao = false

        _.each(Object.keys(window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos), campoNome => {

            let obrigatorio = window.$i18n.getLocaleMessage(window.$i18n.locale)[telaNome].campos[campoNome].obrigatorio

            if (obrigatorio !== undefined) {
                obrigatorio = JSON.parse(obrigatorio)
            }

            if(objetoEntidade[campoNome] === 'GERACAO_INTERNA' || objetoEntidade[campoNome] === 'TRANSACAO_SEM_CONTRAPRESTACAO'){
                geracaoOuTransacao = true
            }
            if ((campoNome === 'dataVencimento' && !objetoEntidade[campoNome] && !objetoEntidade.vidaIndefinida && !geracaoOuTransacao)){
                valido = false
            }
            if (campoNome !== 'dataVencimento' && campoNome === 'dataAtivacao' && permissaoRetroativa && !permissaoRetroativaExpirada && !objetoEntidade[campoNome]){
                valido = false
            }
            if (campoNome !== 'dataVencimento' && campoNome !== 'dataAtivacao' && obrigatorio && !objetoEntidade[campoNome]){
                valido = false
            }
            if (campoNome !== 'dataVencimento' && campoNome !== 'dataAtivacao' &&  campoNome !== 'valorEntrada' && obrigatorio === undefined  && !objetoEntidade[campoNome]){
                valido = false
            }
            if (objetoEntidade[campoNome]) {
                if (obrigatorio && campoNome === 'numeroNL' && objetoEntidade[campoNome].length !== 12){
                    valido = false
                }
            }
        })

        return valido
    }
}
