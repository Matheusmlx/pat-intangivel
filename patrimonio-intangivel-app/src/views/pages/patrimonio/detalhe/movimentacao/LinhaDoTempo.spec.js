import {shallowMount} from '@vue/test-utils'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import LinhaDoTempo from './LinhaDoTempo'

describe('LinhaDoTempo.vue', () => {

    let localVue, wrapper, router, $gtag

    const items = [
        {
            id: 1,
            tipo: 'TRANSFERENCIA_DEFINITIVA',
            situacao: 'EM_ELABORACAO',
            data: '11/11/1111',
            motivo: 'teste',
            orgaoDestino: {
                codHierarquia: '0002.0005.0007',
                descricao: 'AEM-MS - Agência Estadual de Metrologia',
                id: 129,
                nome: 'Agência Estadual de Metrologia',
                sigla: 'AEM-MS'
            },
            orgaoOrigem: {
                codHierarquia: '0002.0005.0007',
                descricao: 'AEM-MS - Agência Estadual de Metrologia',
                id: 129,
                nome: 'Agência Estadual de Metrologia',
                sigla: 'AEM-MS'
            }
        },
        {
            id: 2,
            tipo: 'DOACAO_ENTRE_ORGAOS',
            situacao: 'FINALIZADO',
            data: '11/11/1111',
            motivo: 'teste',
            orgaoDestino: {
                codHierarquia: '0002.0005.0007',
                descricao: 'AEM-MS - Agência Estadual de Metrologia',
                id: 129,
                nome: 'Agência Estadual de Metrologia',
                sigla: 'AEM-MS'
            },
            orgaoOrigem: {
                codHierarquia: '0002.0005.0007',
                descricao: 'AEM-MS - Agência Estadual de Metrologia',
                id: 129,
                nome: 'Agência Estadual de Metrologia',
                sigla: 'AEM-MS'
            }
        },
        {
            id: 3,
            tipo: 'DOACAO_ENTRE_ORGAOS',
            situacao: 'FINALIZADO',
            data: '11/11/1111',
            motivo: 'teste',
            orgaoDestino: {
                codHierarquia: '0002.0005.0007',
                descricao: 'AEM-MS - Agência Estadual de Metrologia',
                id: 129,
                nome: 'Agência Estadual de Metrologia',
                sigla: 'AEM-MS'
            },
            orgaoOrigem: {
                codHierarquia: '0002.0005.0007',
                descricao: 'AEM-MS - Agência Estadual de Metrologia',
                id: 129,
                nome: 'Agência Estadual de Metrologia',
                sigla: 'AEM-MS'
            }
        }
    ]

    beforeEach(() => {

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'MenuDetalhe',
                    params: {
                        id: 1
                    }
                }
            }
        }

        $gtag = {
            event: jest.fn()
        }

        localVue = applicationTestBuilder.build()
    })

    describe('methods', () => {

        it('deve retornar true se a situação da movimentação for igual a EM_ELABORACAO', () => {
            wrapper = shallowMount(LinhaDoTempo, {
                localVue,
                mocks: {
                    $gtag
                },
                propsData: {
                    items: items
                }
            })
            const movimentacao = {situacao: 'EM_ELABORACAO'}
            expect(wrapper.vm.ehMovimentacaoEmElaboracao(movimentacao)).toEqual(true)
        })

        it('deve retornar false se a situação da movimentação for diferente de EM_ELABORACAO', () => {
            wrapper = shallowMount(LinhaDoTempo, {
                localVue,
                mocks: {
                    $gtag
                },
                propsData: {
                    items: items
                }
            })
            const movimentacao = {situacao: 'FINALIZADO'}
            expect(wrapper.vm.ehMovimentacaoEmElaboracao(movimentacao)).toEqual(false)
        })

        it('deve setar false em verMaisLinhaDoTempo quando o usuario clicar em ver mais e carregar todos os itens da linha do tempo', () => {
            wrapper = shallowMount(LinhaDoTempo, {
                localVue,
                mocks: {
                    $gtag
                },
                propsData: {
                    items: items
                }
            })
            wrapper.vm.verMaisLinhaDoTempo()

            expect(wrapper.vm.movimentacoes).toEqual(items)
            expect(wrapper.vm.verMais).toEqual(false)
        })

        it('deve formatar a movimentação para o modal da movimentação', () => {
            wrapper = shallowMount(LinhaDoTempo, {
                localVue,
                router,
                mocks: {
                    $gtag
                },
                propsData: {
                    items: items
                }
            })
            const item = {
                id: 1,
                orgaoOrigem: {
                    codHierarquia: '0002.0005.0007',
                    descricao: 'AEM-MS - Agência Estadual de Metrologia',
                    id: 129,
                    nome: 'Agência Estadual de Metrologia',
                    sigla: 'AEM-MS'
                },
                orgaoDestino: {
                    codHierarquia: '0002.0005.0007',
                    descricao: 'AEM-MS - Agência Estadual de Metrologia',
                    id: 129,
                    nome: 'Agência Estadual de Metrologia',
                    sigla: 'AEM-MS'
                },
                idPatrimonio: 1,
                motivo: 'teste',
                tipo: 'TRANSFERENCIA_DEFINITIVA'
            }
            wrapper.vm.formatarMovimentacao(item)

            expect(wrapper.vm.movimentacaoSelecionada.id).toEqual(item.id)
            expect(wrapper.vm.movimentacaoSelecionada.orgaoDestino).toEqual(item.orgaoDestino)
            expect(wrapper.vm.movimentacaoSelecionada.orgaoOrigem).toEqual(item.orgaoOrigem.id)
            expect(wrapper.vm.movimentacaoSelecionada.idPatrimonio).toEqual(item.idPatrimonio)
            expect(wrapper.vm.movimentacaoSelecionada.motivo).toEqual(item.motivo)
            expect(wrapper.vm.movimentacaoSelecionada.tipo).toEqual(item.tipo)
        })

        it('deve formatar a linha do tempo quando houver mais de cinco movimentações e habilitar o botão de ver mais', () => {
            const movimentacoes = items.concat(items)
            wrapper = shallowMount(LinhaDoTempo, {
                localVue,
                router,
                mocks: {
                    $gtag
                },
                propsData: {
                    items: movimentacoes
                }
            })
            wrapper.vm.formatarLinhaDoTempo()

            expect(wrapper.vm.movimentacoes).toEqual(movimentacoes.slice(0, 5))
            expect(wrapper.vm.verMais).toEqual(true)
        })

        it('não deve formatar a linha do tempo quando houver menos de cinco movimentações', () => {
            wrapper = shallowMount(LinhaDoTempo, {
                localVue,
                router,
                mocks: {
                    $gtag
                },
                propsData: {
                    items: items
                }
            })
            wrapper.vm.formatarLinhaDoTempo()

            expect(wrapper.vm.movimentacoes).toEqual(items)
            expect(wrapper.vm.verMais).toEqual(false)
        })
    })
})
