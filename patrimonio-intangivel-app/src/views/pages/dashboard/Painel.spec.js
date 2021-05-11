import {shallowMount} from '@vue/test-utils'
import Painel from './Painel'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {actionTypes} from '@/core/constants'

describe('Painel.vue', () => {

    let localVue, wrapper, state, actions, store, router

    const totalizadores = {
        emElaboracao: 1,
        totalDeRegistros: 1,
        ativos: 1
    }

    const licencas = [ {
        id:1,
        nome: 'Alura',
        diasParaVencer:12
    }]

    const dados = [{
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

        actions = {
            [actionTypes.DASHBOARD.BUSCAR_TOTALIZADORES]: jest.fn().mockReturnValue(totalizadores),
            [actionTypes.DASHBOARD.BUSCAR_LICENCAS_PROXIMAS_A_VENCER]: jest.fn().mockReturnValue(licencas),
            [actionTypes.DASHBOARD.BUSCAR_INTANGIVEIS_POR_ORGAO]: jest.fn().mockReturnValue(dados),
        }

        state = {
            licencas:[],
            patrimonio: {
                todosPatrimonios: {
                    filtros: {
                        conteudo: {
                            value: null
                        }
                    },
                    paginacao: {
                        sortBy: ['situacao'],
                        sortDesc: [false],
                        descending: true
                    }
                }
            }
        }

        store = new Vuex.Store({state, actions})
    })

    describe('methods', () => {

        it('deve buscar os totalizadores para os card rápidos', async () => {
            wrapper = shallowMount(Painel, {
                localVue,
                store
            })

            await wrapper.vm.buscarTotalizadores()

            expect(actions[actionTypes.DASHBOARD.BUSCAR_TOTALIZADORES]).toHaveBeenCalled()
        })

        it('deve buscar as licencas que estão proximas para vencer', async () => {
            wrapper = shallowMount(Painel, {
                localVue,
                store
            })
            await wrapper.vm.buscarLicencasProximasAVencer()
            expect(actions[actionTypes.DASHBOARD.BUSCAR_LICENCAS_PROXIMAS_A_VENCER]).toHaveBeenCalled()
        })

        it('deve redirecionar para a tela de listagem dos patrimônio sem filtro', async () => {
            wrapper = shallowMount(Painel, {
                localVue,
                router,
                store
            })

            await wrapper.vm.filtrarPatrimonioPorTotalizadores()
            expect(wrapper.vm.$store.state.patrimonio.todosPatrimonios.filtros.conteudo.value).toEqual(null)
            expect(router.push.mock.calls[0][0]).toEqual({name: 'TodosPatrimonios'})
        })

        it('deve acessar a ficha do patrimonio que foi selecionado', async () => {
            wrapper = shallowMount(Painel,{
                localVue,
                router,
                store
            })

            await  wrapper.vm.acessarLicenca(1)
            expect(router.push.mock.calls[0][0]).toEqual({name: 'FichaPatrimonio',params:{id:1}})
        })

        it('deve redirecionar para a tela de listagem dos patrimônio com o filtro elaboração', async () => {
            wrapper = shallowMount(Painel, {
                localVue,
                router,
                store
            })

            await wrapper.vm.filtrarPatrimonioPorTotalizadores('Elaboração')
            expect(wrapper.vm.$store.state.patrimonio.todosPatrimonios.filtros.conteudo.value).toEqual('Elaboração')
            expect(router.push.mock.calls[0][0]).toEqual({name: 'TodosPatrimonios'})
        })

        it('deve redirecionar para a tela de listagem dos patrimônio com o filtro Ativo', async () => {
            wrapper = shallowMount(Painel, {
                localVue,
                router,
                store
            })

            await wrapper.vm.filtrarPatrimonioPorTotalizadores('Ativo')
            expect(wrapper.vm.$store.state.patrimonio.todosPatrimonios.filtros.conteudo.value).toEqual('Ativo')
            expect(router.push.mock.calls[0][0]).toEqual({name: 'TodosPatrimonios'})
        })

        it('deve redirecionar para a listagem dos patrimônios', async () => {
            wrapper = shallowMount(Painel, {
                localVue,
                router,
                store
            })

            await wrapper.vm.verTodos()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'TodosPatrimonios'})
        })

        it('deve buscar dados pra preencher o gráfico em barra', async () => {
            wrapper = shallowMount(Painel, {
                localVue,
                router,
                store
            })

            await wrapper.vm.buscarDadosParaOGrafico()
            expect(actions[actionTypes.DASHBOARD.BUSCAR_INTANGIVEIS_POR_ORGAO]).toHaveBeenCalled()
        })

        it('deve setar a ordenação por órgão na listagem de intangíveis', () => {
            wrapper = shallowMount(Painel, {
                localVue,
                router,
                store
            })

            wrapper.vm.ordenarListaPorOrgao()
            expect(state.patrimonio.todosPatrimonios.paginacao.descending).toEqual(false)
            expect(state.patrimonio.todosPatrimonios.paginacao.sortBy[0]).toEqual('orgao')
            expect(state.patrimonio.todosPatrimonios.paginacao.sortDesc[0]).toEqual(true)
        })
    })
})
