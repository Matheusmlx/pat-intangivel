import  dashBoardApiClient from './DashboardApiClient'
import dashboardApiClient from "@/core/apiclient/DashboardApiClient";

let url, mockRetornoApi, verboHttp

jest.mock('axios', ()=>({
    get(_url){
        return new Promise(resolve =>{
            verboHttp = 'get'
            url = _url
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('DashBoardApiClient', ()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('buscarLicencasProximasAVencer',async()=>{
        const {data} = await dashBoardApiClient.buscarLicencasProximasAVencer()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/dashboard/licenca/proximas-vencer?quantidade=5')
    })
    it('buscarMovimentacaoPorPatrimonio', async () => {
        const {data} = await dashboardApiClient.buscarTotalizadores()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/dashboard/totalizadores')
    })

    it('buscarIntangiveisPorOrgao', async () => {
        const {data} = await dashboardApiClient.buscarIntangiveisPorOrgao()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/dashboard/metricas-por-orgao')
    })

    it('buscarIntangiveisPorTipo', async () => {
        const {data} = await  dashBoardApiClient.buscarIntangiveisPorTipo()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/dashboard/totalizadores-por-tipo')
    })
})
