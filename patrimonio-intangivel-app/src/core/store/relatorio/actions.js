import api from '@/core/apiclient'
import {actionTypes, extensaoArquivo} from '@/core/constants'

export default {
    async [actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_PDF](context, filtro) {
        const {data} = await api.relatorio.baixarRelatorioInventarioAnalitico(filtro)
        let blob = new Blob([data], {
            type: extensaoArquivo.APPLICATION_PDF
        })
        const downloadLink = document.createElement('a')
        document.body.appendChild(downloadLink)
        downloadLink.setAttribute('type', 'hidden')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = `inventario-analitico-${filtro.mesReferencia.value}.pdf`
        downloadLink.click()
    },

    async [actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_XLS](context, filtro) {
        const {data} = await api.relatorio.baixarRelatorioInventarioAnalitico(filtro)
        let blob = new Blob([data], {
            type: extensaoArquivo.APPLICATION_X_MSEXCEL
        })
        const downloadLink = document.createElement('a')
        document.body.appendChild(downloadLink)
        downloadLink.setAttribute('type', 'hidden')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = `inventario-analitico-${filtro.mesReferencia.value}.xls`
        downloadLink.click()
    },

    async [actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_PDF](context, filtro) {
        const {data} = await api.relatorio.baixarRelatorioInventarioSintetico(filtro)
        let blob = new Blob([data], {
            type: extensaoArquivo.APPLICATION_PDF
        })
        const downloadLink = document.createElement('a')
        document.body.appendChild(downloadLink)
        downloadLink.setAttribute('type', 'hidden')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = `inventario-sintetico-${filtro.mesReferencia.value}.pdf`
        downloadLink.click()
    },

    async [actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_XLS](context, filtro) {
        const {data} = await api.relatorio.baixarRelatorioInventarioSintetico(filtro)
        let blob = new Blob([data], {
            type: extensaoArquivo.APPLICATION_X_MSEXCEL
        })
        const downloadLink = document.createElement('a')
        document.body.appendChild(downloadLink)
        downloadLink.setAttribute('type', 'hidden')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = `inventario-sintetico-${filtro.mesReferencia.value}.xls`
        downloadLink.click()
    },

    async [actionTypes.RELATORIO.BAIXAR_RELATORIO_MEMORANDO_PDF](context, patrimonio) {
        const {data} = await api.relatorio.baixarRelatorioMemorandoPDF(patrimonio)
        let blob = new Blob([data], {
            type: extensaoArquivo.APPLICATION_PDF
        })
        const downloadLink = document.createElement('a')
        document.body.appendChild(downloadLink)
        downloadLink.setAttribute('type', 'hidden')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = 'relatorio-memorando.pdf'
        downloadLink.click()
    },

    async [actionTypes.RELATORIO.BAIXAR_RELATORIO_MEMORANDO_MOVIMENTACAO_PDF](context, movimentacao) {
        const {data} = await api.relatorio.baixarRelatorioMemorandoMovimentacaoPDF(movimentacao)
        let blob = new Blob([data], {
            type: extensaoArquivo.APPLICATION_PDF
        })
        const downloadLink = document.createElement('a')
        document.body.appendChild(downloadLink)
        downloadLink.setAttribute('type', 'hidden')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = 'relatorio-memorando-movimentacao.pdf'
        downloadLink.click()
    }
}
