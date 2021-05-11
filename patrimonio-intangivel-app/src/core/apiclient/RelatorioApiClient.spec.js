import RelatorioApiClient from './RelatorioApiClient'

let url, config, mockRetornoApi, verboHttp

jest.mock('axios', ()=>({
    get(_url){
        return new Promise(resolve =>{
            verboHttp = 'get'
            url = _url
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('RelatorioApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('Deve realizar download de relatorio sintetico pdf',async()=>{
        const filtros = {
            tipo: {
                value: 'SINTETICO'
            },
            orgao: {
                value: 129
            },
            mesReferencia: {
                value: '2020-04'
            },
            formato:{
                value: 'PDF'
            }
        }
        const {data} = await RelatorioApiClient.baixarRelatorioInventarioSintetico(filtros)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/relatorios/inventario/sintetico?tipo=${filtros.tipo.value}&orgao=${filtros.orgao.value}&mesReferencia=${filtros.mesReferencia.value}&formato=${filtros.formato.value}`)
    })
    it('Deve realizar download de relatorio sintetico xls',async()=>{
        const filtros = {
            tipo: {
                value: 'SINTETICO'
            },
            orgao: {
                value: 129
            },
            mesReferencia: {
                value: '2020-04'
            },
            formato: {
                value: 'XLS'
            }
        }
        const {data} = await RelatorioApiClient.baixarRelatorioInventarioSintetico(filtros)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/relatorios/inventario/sintetico?tipo=${filtros.tipo.value}&orgao=${filtros.orgao.value}&mesReferencia=${filtros.mesReferencia.value}&formato=${filtros.formato.value}`)
    })

    it('Deve realizar download de relatorio analitico',async()=>{
        const filtros = {
            tipo: {
                value: 'ANALITICO'
            },
            orgao: {
                value: 129
            },
            mesReferencia: {
                value: '2020-04'
            },
            formato: {
                value: 'PDF'
            }
        }
        const {data} = await RelatorioApiClient.baixarRelatorioInventarioAnalitico(filtros)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/relatorios/inventario/analitico?tipo=${filtros.tipo.value}&orgao=${filtros.orgao.value}&mesReferencia=${filtros.mesReferencia.value}&formato=${filtros.formato.value}`)
    })

    it('Deve realizar download de relatorio memorando pdf',async()=>{
        const patrimonio = '2'
        const {data} = await RelatorioApiClient.baixarRelatorioMemorandoPDF(patrimonio)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/relatorios/memorando/pdf/${patrimonio}`)
    })

    it('Deve realizar download de relatorio memorando da movimentação pdf', async () => {
        const movimentacao = 1
        const {data} = await RelatorioApiClient.baixarRelatorioMemorandoMovimentacaoPDF(movimentacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/relatorios/memorando/movimentacao/pdf/${movimentacao}`)
    })
})
