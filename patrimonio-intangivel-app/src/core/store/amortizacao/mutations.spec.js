import mutations from './mutations'
import { mutationTypes } from '@/core/constants'

const state = {
    patrimonios:{}
}

describe('Mutations', () => {
    it('Deve setar os patrimonios amortizados', () => {
        const patrimonios = [{
            id:1,
            nome:'Alura Curso',
            tipo: 'MARCA'},
            {
                id:2,
                nome:'Adobe Premiere',
                tipo:'SOFTWARE'
            }
        ]

        mutations[mutationTypes.AMORTIZACAO.SET_PATRIMONIOS_AMORTIZADOS](state,patrimonios)
        expect(state.patrimonios).toEqual(patrimonios)
    })
})
