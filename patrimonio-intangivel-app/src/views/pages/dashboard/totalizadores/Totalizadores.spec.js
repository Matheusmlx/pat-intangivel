import {shallowMount} from '@vue/test-utils'
import Totalizadores from './Totalizadores'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('Totalizadores.vue', () => {

    let localVue, wrapper

    const totalizadores = {
        ativos: 0,
        emElaboracao: 0,
        totalDeRegistros: 0
    }


    beforeEach(() => {
        localVue = applicationTestBuilder.build()
    })

    describe('renderização', () => {

        it('deve mostrar os card com os totalizadores', () => {
            wrapper = shallowMount(Totalizadores, {
                localVue,
                propsData: {
                    totalizadores : totalizadores
                }
            })

            expect(wrapper.findAll('v-card-stub').at(0).text()).toContain('TOTAL DE REGISTROS')
            expect(wrapper.findAll('v-icon-stub').at(0).text()).toContain('add_circle_outline')
            expect(wrapper.findAll('v-card-stub').at(1).text()).toContain('EM ELABORAÇÃO')
            expect(wrapper.findAll('v-icon-stub').at(1).text()).toContain('far fa-edit')
            expect(wrapper.findAll('v-card-stub').at(2).text()).toContain('ATIVOS')
            expect(wrapper.findAll('v-icon-stub').at(2).text()).toContain('far fa-check-circle')
        })
    })

    describe('methods', () => {

        it('deve emitir o evento de filtrarPatrimonioPorTotalizadores', () => {
            wrapper = shallowMount(Totalizadores, {
                localVue,
                propsData: {
                    totalizadores : totalizadores
                }
            })

            wrapper.vm.filtrarPorTodos()

            expect(wrapper.emitted().filtrarPatrimonioPorTotalizadores).toBeTruthy()
        })

        it('deve emitir o evento de filtrarPatrimonioPorTotalizadores para filtrar por patrimônios em elaboração', () => {
            wrapper = shallowMount(Totalizadores, {
                localVue,
                propsData: {
                    totalizadores : totalizadores
                }
            })

            wrapper.vm.filtrarPorEmElaboracao()

            expect(wrapper.emitted().filtrarPatrimonioPorTotalizadores).toBeTruthy()
            expect(wrapper.emitted().filtrarPatrimonioPorTotalizadores[0]).toEqual(['Elaboração'])
        })

        it('deve emitir o evento de filtrarPatrimonioPorTotalizadores para filtrar por patrimônios ativos', () => {
            wrapper = shallowMount(Totalizadores, {
                localVue,
                propsData: {
                    totalizadores : totalizadores
                }
            })

            wrapper.vm.filtrarPorAtivos()

            expect(wrapper.emitted().filtrarPatrimonioPorTotalizadores).toBeTruthy()
            expect(wrapper.emitted().filtrarPatrimonioPorTotalizadores[0]).toEqual(['Ativo'])
        })
    })
})
