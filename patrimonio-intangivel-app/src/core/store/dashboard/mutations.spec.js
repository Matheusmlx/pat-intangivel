import {mutationTypes} from '@/core/constants'
import mutations from './mutations'

const state = {
    licencas:[]
}

const licencas = [
    {
        id:1,
        nome:' Adobe Photoshop 2020',
        diasParaVencer:2
    },
    {
        id:2,
        nome:'LifeStyle Software',
        diasParaVencer:10
    },
    {
        id:3,
        nome:'Mindbody Peacefull',
        diasParaVencer:38
    },
    {
        id:4,
        nome:'Caixa Premia Excelsior',
        diasParaVencer:180
    },
]

describe('Mutations', () => {
    it('Deve setar as licenças no state', () => {
        mutations[mutationTypes.DASHBOARD.SET_LICENCAS](state,licencas)
        expect(state.licencas).toEqual(licencas)
    })
})
