import {mount} from '@vue/test-utils'
import MovimentacaoDetalhePatrimonios from './MovimentacaoDetalhePatrimonios'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('MovimentacaoDetalhePatrimonios.vue', () => {

    let localVue, wrapper

    const patrimonio = {
        id: 129,
        nome: 'Agência Estadual de Metrologia',
        numero: '000000001',
        setor: {
            descricao: 'ASCOM - Assessoria de Comunicação Social',
            id: 1
        }
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
    })

    describe('renderização', () => {

        it('deve renderizar a aba de patrimônios', () => {
            wrapper = mount(MovimentacaoDetalhePatrimonios, {
                localVue,
                propsData: {
                    patrimonio: patrimonio
                }
            })

            expect(wrapper.findAll('th').at(0).text()).toContain('N° Patrimônio')
            expect(wrapper.findAll('th').at(1).text()).toContain('Nome Patrimônio')
            expect(wrapper.findAll('td').at(0).text()).toContain('000000001')
            expect(wrapper.findAll('td').at(1).text()).toContain('Agência Estadual de Metrologia')
        })
    })
})
