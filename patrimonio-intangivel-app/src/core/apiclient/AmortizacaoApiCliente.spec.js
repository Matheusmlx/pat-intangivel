import amortizacaoApi from './AmortizacaoApiClient'

let url, config, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    post(_url) {
        return new Promise(resolve => {
            verboHttp = 'post'
            url = _url
            resolve({data: mockRetornoApi})
        })
    }
}))

describe('AmortizacaoApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('disparar', async ()=>{
        const dados = {orgao:{id:2}}
        const { data} = await amortizacaoApi.disparar(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/amortizacao-manual')
    })
})
