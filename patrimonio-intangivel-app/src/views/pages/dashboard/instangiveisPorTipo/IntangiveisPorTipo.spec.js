import {shallowMount} from '@vue/test-utils'
import IntangivelPorTipo from './IntangiveisPorTipo'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('IntangivelPorTipo.vue', () => {

    let localVue, wrapper, router

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'Painel'
                }
            }
        }
    })

    describe('computed', () => {

        it('deve retornar true se dados tiver mais que um intangivel', () => {
            wrapper = shallowMount(IntangivelPorTipo, {
                localVue,
                router,
                propsData: {
                    patrimoniosPorTipo: [
                        {
                            nome: 'Software',
                            quantidade: 2
                        },
                        {
                            nome: 'Marcas',
                            quantidade: 4
                        }
                    ]
                }
            })

            expect(wrapper.vm.exibirGrafico).toEqual(true)
        })

        it('deve retornar false se dados tiver nenhum ou apenas um intangivel', () => {
            wrapper = shallowMount(IntangivelPorTipo, {
                localVue,
                router,
                propsData: {
                    patrimoniosPorTipo: [
                        {
                            nome: 'Software',
                            quantidade: 0
                        }
                    ]
                }
            })

            expect(wrapper.vm.exibirGrafico).toEqual(false)
        })
    })

    describe('methods', () => {

        it('deve emitir o evento de redirecionar para a listagem dos patrimônios', async () => {
            wrapper = shallowMount(IntangivelPorTipo, {
                localVue,
                router
            })

            await wrapper.vm.verTodos()
            expect(wrapper.emitted().verTodos).toBeTruthy()
        })
    })
})
