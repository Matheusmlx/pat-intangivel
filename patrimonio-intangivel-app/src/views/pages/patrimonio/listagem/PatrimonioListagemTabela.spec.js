import {mount, shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import ApplicationTestBuilder from '../../../../application/ApplicationTestBuilder'
import PatrimonioListagemTabela from './PatrimonioListagemTabela'
import {mutationTypes} from '@/core/constants'

let criarStore = ({ state, getters, mutations, actions }) => {
    return new Vuex.Store({ state, getters, mutations, actions })
}

describe('PatrimonioListagemTabela', () => {
    let wrapper, state, localVue, vuetify, mutations, tipos, $gtag

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                },
            },
        }

        mutations = {
            [mutationTypes.PATRIMONIO.RESETA_PAGE]: jest.fn()
        }

        $gtag = {
            event: jest.fn()
        }

        localVue = ApplicationTestBuilder.build()
        vuetify = ApplicationTestBuilder.getVuetify()

        tipos = {
            'DIREITOS_AUTORAIS': {
                nome: 'Direitos Autorais',
                icone: 'far fa-copyright'
            },
            'RECEITAS_FORMULAS_PROJETOS': {
                nome: 'Receitas, Fórmulas E Projetos',
                icone: 'fas fa-file-alt'
            },
            'TITULOS_DE_PUBLICACAO': {
                nome: 'Títulos de Publicação',
                icone: 'fas fa-book-open'
            },
            'LICENCAS': {
                nome: 'Licenças',
                icone: 'fas fa-key'
            },
            'SOFTWARES': {
                nome: 'Softwares',
                icone: 'mouse'
            },
            'MARCAS': {
                nome: 'Marcas',
                icone: 'far fa-registered'
            }
        }
    })

    describe('Events', () => {
        it('Deve emitir o evento de acessar passando o patrimonio ', () => {
            wrapper = mount(PatrimonioListagemTabela, {
                localVue,
                vuetify,
                store: criarStore({ state }),
                mocks: {
                    $gtag
                },
                propsData: {
                    itens: [
                        {
                            id: 0,
                            numero: '--',
                            nome: 'Adobe Illustrator 2020',
                            situacao: 'Em desenvolvimento',
                            orgao: 'SEJUSP',
                            tipo: 'SOFTWARES'
                        },
                    ],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                    tipos
                },
                sync: false,
            })

            const patrimonio = wrapper
                .find('table')
                .findAll('tr')
                .at(1)
            patrimonio.trigger('click')
            expect(wrapper.emitted().acessar).toBeTruthy()
            expect(wrapper.emitted().acessar[0]).toEqual([
                {
                    id: 0,
                    numero: '--',
                    nome: 'Adobe Illustrator 2020',
                    situacao: 'Em desenvolvimento',
                    orgao: 'SEJUSP',
                    tipo: 'SOFTWARES'
                },
            ])
        })
    })

    describe('Props', () => {
        it('Deve exibir a mensagem padrão quando não existir nenhum patrimonio', () => {
            wrapper = mount(PatrimonioListagemTabela, {
                localVue,
                vuetify,
                mocks: {
                    $gtag
                },
                store: criarStore({ state }),
                propsData: {
                    itens: [],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 0,
                    totalItens: 0,
                    tipos
                },
                sync: false,
            })

            const table = wrapper
                .find('table')
                .findAll('tr')
                .at(1)
                .findAll('td')
            expect(table.at(0).text()).toEqual('Não há patrimônios cadastrados')
        })

        it('Deve renderizar as informações na tabela', () => {
            wrapper = mount(PatrimonioListagemTabela, {
                localVue,
                vuetify,
                store: criarStore({ state }),
                mocks: {
                    $gtag
                },
                propsData: {
                    itens: [
                        {
                            id: 23,
                            nome: 'patrimonio de Treinamento 2.0',
                            numero: '123',
                            tipo: 'SOFTWARES',
                            situacao: 'ATIVO'
                        },
                    ],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                    tipos
                },
                sync: false,
            })

            const colunastabela = wrapper
                .find('table')
                .findAll('tr')
                .at(1)
                .findAll('td')
            expect(colunastabela.length).toEqual(6)
            expect(colunastabela.at(0).text()).toEqual('123')
            expect(colunastabela.at(1).text()).toEqual('patrimonio de Treinamento 2.0')
            expect(colunastabela.at(3).text()).toEqual('Ativo')
            expect(colunastabela.at(4).text()).toEqual('mouse\n                Softwares')
        })
    })

    describe('Watch', () => {
        it('Deve emitir o evento de paginar quando alterar a paginação', () => {
            wrapper = shallowMount(PatrimonioListagemTabela, {
                localVue,
                vuetify,
                store: criarStore({ state }),
                mocks: {
                    $gtag
                },
                propsData: {
                    itens: [],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 0,
                    totalItens: 0,
                    tipos
                },
            })

            wrapper.vm.paginacaoInterna.rowsPerPage = 20
            expect(wrapper.emitted().paginar)
        })

        it('Deve emitir o evento filtrarPorTipo quando ocorrer a filtragem', () => {
            wrapper = shallowMount(PatrimonioListagemTabela, {
                localVue,
                vuetify,
                store: criarStore({ state }),
                mocks: {
                    $gtag
                },
                propsData: {
                    itens: [],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 0,
                    totalItens: 0,
                    tipos
                }
            })

            wrapper.vm.tipoPatrimonio = 'SOFTWARES'

            wrapper.vm.tratarFiltro()

            expect(wrapper.emitted().filtrarPorTipo).toBeTruthy()
            expect(wrapper.emitted().filtrarPorTipo).toEqual([['SOFTWARES']])
        })

        it('Deve resetar o page para 1 ao mudar a quantidade de linhas por página', () => {
            wrapper = shallowMount(PatrimonioListagemTabela, {
                localVue,
                vuetify,
                store: criarStore({ state, mutations }),
                mocks: {
                    $gtag
                },
                propsData: {
                    itens: [],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 0,
                    totalItens: 0,
                    tipos
                }
            })

            wrapper.vm.resetaPage()

            expect(mutations[mutationTypes.PATRIMONIO.RESETA_PAGE]).toHaveBeenCalled()
        })
    })

    describe('Filters', () => {
        it('Deve retornar número do patrimônio', () => {
            wrapper = shallowMount(PatrimonioListagemTabela, {
                localVue,
                vuetify,
                store: criarStore({ state, mutations }),
                mocks: {
                    $gtag
                },
                propsData: {
                    itens: [],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 0,
                    totalItens: 0,
                    tipos
                }
            })
            const codigo = 1
            const numero = '0000000001'

            expect(wrapper.vm.$options.filters.retornaCodigoOuNumero(codigo,numero)).toEqual(numero)
        })

        it('Deve retornar código de registro', () => {
            wrapper = shallowMount(PatrimonioListagemTabela, {
                localVue,
                vuetify,
                store: criarStore({ state, mutations }),
                mocks: {
                    $gtag
                },
                propsData: {
                    itens: [],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 0,
                    totalItens: 0,
                    tipos
                }
            })
            const codigo = 1
            const numero = null

            expect(wrapper.vm.$options.filters.retornaCodigoOuNumero(codigo,numero)).toEqual(1)
        })

    })
})
