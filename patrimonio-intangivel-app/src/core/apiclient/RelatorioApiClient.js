import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'

class RelatorioApiClient {

    async baixarRelatorioInventarioAnalitico(filtros) {
        const url = AzSearchUrlBuilder.build(
            'api/relatorios/inventario/analitico',
            filtros,
            {}
        )
        return axios.get(url,
            {
                responseType: 'arraybuffer'
            })
    }

    async baixarRelatorioInventarioSintetico(filtros) {
        const url = AzSearchUrlBuilder.build(
            'api/relatorios/inventario/sintetico',
            filtros
        )
        return axios.get(url,
            {
                responseType: 'arraybuffer'
            })
    }

    async baixarListagemPatrimonioXLS(filtros,paginacao){
        paginacao.descending = !paginacao.sortDesc[0]
        const url = AzSearchUrlBuilder.build('api/relatorios/listagemPatrimonio/xls',filtros,paginacao)
        return axios.get(url,{responseType:'arraybuffer'})
    }

    async baixarRelatorioMemorandoPDF(patrimonio) {
        const url = `api/relatorios/memorando/pdf/${patrimonio}`
        return axios.get(url, {responseType: 'arraybuffer'})
    }

    async baixarRelatorioMemorandoMovimentacaoPDF(movimentacao) {
        const url = `api/relatorios/memorando/movimentacao/pdf/${movimentacao}`
        return axios.get(url, {responseType: 'arraybuffer'})
    }
}

export default new RelatorioApiClient()
