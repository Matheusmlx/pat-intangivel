import OrgaoApiClient from './OrgaoApiClient'

let url, config, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({data: mockRetornoApi})
        })
    }
}))

describe('OrgaoApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('buscarTodos',async()=>{
        const {data} = await OrgaoApiClient.buscarTodos()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/unidades-organizacionais?sort=sigla&direction=ASC')
    })
})
