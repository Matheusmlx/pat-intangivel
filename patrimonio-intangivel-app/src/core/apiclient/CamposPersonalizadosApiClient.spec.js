import camposPersonalizadosApi from './CamposPersonalizadosApiClient'

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

describe('PatrimonioApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('deve chamar a api camposPersonalizados e ter o retorno esperado',async()=>{
        const { data } = await camposPersonalizadosApi.getAll()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/rotulosPersonalizados')
    })
})
