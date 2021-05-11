import actions from './actions'
import {actionTypes,mutationTypes} from '@/core/constants'

let url, body, state, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    get(_url){
        return new Promise( resolve => {
            url = _url
            resolve({data:mockResponseData})
        })
    }
}))

describe('Actions', () => {
    const commit = jest.fn()

   beforeEach(() => {
       url = ''
       body = undefined
       mockResponseData = true

       state = {
           licencas:{}
       }
   })

    it('BUSCAR LICENCAS QUE ESTÃO PARA VENCER ', async () => {
        returnedResponse = await actions[actionTypes.DASHBOARD.BUSCAR_LICENCAS_PROXIMAS_A_VENCER]({commit})

        expect(url).toBe('api/dashboard/licenca/proximas-vencer?quantidade=5')
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(commit).toHaveBeenCalledWith(mutationTypes.DASHBOARD.SET_LICENCAS,returnedResponse)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('BUSCAR_TOTALIZADORES', async () => {
        returnedResponse = await actions[actionTypes.DASHBOARD.BUSCAR_TOTALIZADORES]()

        expect(url).toBe('api/dashboard/totalizadores')
        expect(returnedResponse).toBeTruthy()
    })

    it('BUSCAR_INTANGIVEIS_POR_ORGAO', async () => {
        mockResponseData = {itens:[]}
        returnedResponse = await actions[actionTypes.DASHBOARD.BUSCAR_INTANGIVEIS_POR_ORGAO]()

        expect(url).toBe('api/dashboard/metricas-por-orgao')
        expect(returnedResponse).toEqual([])
    })

    it('DEVE BUSCAR INSTAGIVEIS POR TIPO', async () => {
        mockResponseData = {itens:[{nome:'Softwares',quantidade:2,tipo:'SOFTWARES'},{nome:'Direitos Autorais',quantidade:1,tipo:'DIREITOS_AUTORAIS'}]}
        returnedResponse = await actions[actionTypes.DASHBOARD.BUSCAR_INTANGIEIS_POR_TIPO]()

        expect(url).toBe('api/dashboard/totalizadores-por-tipo')
        expect(returnedResponse).toEqual(mockResponseData.itens)
    })

})
