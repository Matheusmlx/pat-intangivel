import SetorApiClient from './SetorApiClient'

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

describe('SetorApiClient', () => {

    beforeEach(() => {
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('buscarTodos', async () => {
        const orgaoId = 1
        const {data} = await SetorApiClient.buscarTodos(orgaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/unidades-organizacionais/${orgaoId}`)
    })
})
