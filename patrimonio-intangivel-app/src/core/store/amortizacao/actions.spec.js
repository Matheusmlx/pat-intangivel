import actions from './actions'
import { actionTypes } from '@/core/constants'

let url, body, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    post(_url, _body) {
        return new Promise((resolve) => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
}))

describe('Actions', () => {
    const context = jest.fn()
    let state

    beforeEach(() => {
        url = ''
        body = undefined
        mockResponseData = true
        state = {
            loki: {
                product: {
                    id: 401,
                    name: 'patrimonio-intangivel'
                }
            }
        }
    })

    it('GERAR_AMORTIZACAO_MANUAL', async () => {
        const patrimonio = {orgao: 109, data: '05/11/2016'}
        returnedResponse = await actions[actionTypes.AMORTIZACAO.GERAR_AMORTIZACAO_MANUAL](context, patrimonio)
        expect(url).toBe('api/amortizacao-manual')
        expect(body).toBe(patrimonio)
    })

})
