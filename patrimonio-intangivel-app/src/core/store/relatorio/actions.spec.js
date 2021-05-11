import {actionTypes} from '@/core/constants'
import actions from './actions'

let url, body, mockResponseData

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            url = _url
            resolve({data: mockResponseData})
        })
    },
}))

describe('Actions', () => {

    const context = jest.fn()

    beforeEach(() => {
        url = ''
        body = undefined
        mockResponseData = true

    })

    it('Deve realizar download de relatorio sintetico pdf', async () => {
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
        window.URL.createObjectURL = jest.fn()
        await actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_PDF](context, filtros)
        expect(url).toBe(`api/relatorios/inventario/sintetico?tipo=${filtros.tipo.value}&orgao=${filtros.orgao.value}&mesReferencia=${filtros.mesReferencia.value}&formato=${filtros.formato.value}`)
    })
    it('Deve realizar download de relatorio sintetico xls', async () => {
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
        window.URL.createObjectURL = jest.fn()
        await actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_SINTETICO_XLS](context, filtros)
        expect(url).toBe(`api/relatorios/inventario/sintetico?tipo=${filtros.tipo.value}&orgao=${filtros.orgao.value}&mesReferencia=${filtros.mesReferencia.value}&formato=${filtros.formato.value}`)
    })
    it('Deve realizar download de relatorio analitico xls', async () => {
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
                value: 'XLS'
            }
        }
        window.URL.createObjectURL = jest.fn()
        await actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_XLS](context, filtros)
        expect(url).toBe(`api/relatorios/inventario/analitico?tipo=${filtros.tipo.value}&orgao=${filtros.orgao.value}&mesReferencia=${filtros.mesReferencia.value}&formato=${filtros.formato.value}`)
    })
    it('Deve realizar download de relatorio analitico pdf', async () => {
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
                value: 'XLS'
            }
        }
        window.URL.createObjectURL = jest.fn()
        await actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_INVENTARIO_ANALITICO_PDF](context, filtros)
        expect(url).toBe(`api/relatorios/inventario/analitico?tipo=${filtros.tipo.value}&orgao=${filtros.orgao.value}&mesReferencia=${filtros.mesReferencia.value}&formato=${filtros.formato.value}`)
    })
    it('Deve realizar download de relatorio memorando pdf', async () => {
        const patrimonio = '1'
        window.URL.createObjectURL = jest.fn()
        await actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_MEMORANDO_PDF](context,patrimonio)
        expect(url).toBe(`api/relatorios/memorando/pdf/${patrimonio}`)
    })
    it('Deve realizar download de relatorio memorando da movimentação pdf', async () => {
        const movimentacao = 1
        window.URL.createObjectURL = jest.fn()
        await actions[actionTypes.RELATORIO.BAIXAR_RELATORIO_MEMORANDO_MOVIMENTACAO_PDF](context, movimentacao)
        expect(url).toBe(`api/relatorios/memorando/movimentacao/pdf/${movimentacao}`)
    })
})
