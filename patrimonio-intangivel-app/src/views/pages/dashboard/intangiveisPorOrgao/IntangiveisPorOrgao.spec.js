import {shallowMount} from '@vue/test-utils'
import IntangiveisPorOrgao from './IntangiveisPorOrgao'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('IntangiveisPorOrgao.vue', () => {

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

        it('deve retornar true se dados tiver mais que um órgão', () => {
            wrapper = shallowMount(IntangiveisPorOrgao, {
                localVue,
                router,
                propsData: {
                    dados: [
                        {
                            idOrgao: 129,
                            sigla: 'AEM-MS',
                            nome: 'Agência Estadual de Metrologia',
                            total: 7,
                            tipos: [
                                {
                                    'nome': 'DIREITOS_AUTORAIS',
                                    'quantidade': 2
                                },
                                {
                                    'nome': 'SOFTWARES',
                                    'quantidade': 5
                                }
                            ]
                        },
                        {
                            idOrgao: 130,
                            sigla: 'AGEPAN',
                            nome: 'Agência Estadual de Regulação de Serviços Públicos de Mato Grosso do Sul',
                            total: 2,
                            tipos: [
                                {
                                    'nome': 'DIREITOS_AUTORAIS',
                                    'quantidade': 2
                                }
                            ]
                        }
                    ]
                }
            })

            expect(wrapper.vm.exibirGrafico).toEqual(true)
        })

        it('deve retornar false se dados tiver nenhum ou apenas um órgão', () => {
            wrapper = shallowMount(IntangiveisPorOrgao, {
                localVue,
                router,
                propsData: {
                    dados: [{
                        idOrgao: 129,
                        sigla: 'AEM-MS',
                        nome: 'Agência Estadual de Metrologia',
                        total: 7,
                        tipos: [
                            {
                                'nome': 'DIREITOS_AUTORAIS',
                                'quantidade': 2
                            },
                            {
                                'nome': 'SOFTWARES',
                                'quantidade': 5
                            }
                        ]
                    }]
                }
            })

            expect(wrapper.vm.exibirGrafico).toEqual(false)
        })
    })

    describe('methods', () => {

        it('deve emitir o evento de redirecionar para a listagem dos patrimônios', async () => {
            wrapper = shallowMount(IntangiveisPorOrgao, {
                localVue,
                router
            })

            await wrapper.vm.verTodos()
            expect(wrapper.emitted().verTodos).toBeTruthy()
        })
    })
})
