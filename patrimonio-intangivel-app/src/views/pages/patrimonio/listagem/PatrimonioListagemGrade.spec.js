import {mount, shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import ApplicationTestBuilder from '../../../../application/ApplicationTestBuilder'
import PatrimonioListagemGrade from './PatrimonioListagemGrade'
import {mutationTypes} from '@/core/constants'

let criarStore = ({ state, getters, mutations, actions }) => {
    return new Vuex.Store({ state, getters, mutations, actions })
}

describe('PatrimonioListagemGrade', () => {
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
            wrapper = mount(PatrimonioListagemGrade, {
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
                sync: false
            })

            const patrimonio = wrapper
                .find('div')
                .findAll('div')
                .at(2)
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
        it('Deve emitir mensagem padrão quando não existir nenhum patrimonio', () => {
            wrapper = mount(PatrimonioListagemGrade, {
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
                sync: false,
            })

            const h2 = wrapper.find('h2')
            expect(h2.text()).toContain('Não há patrimônios cadastrados')
        })

        it('Deve renderizar as informações na tela', () => {
            wrapper = mount(PatrimonioListagemGrade, {
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
                            situacao: 'ABERTO',
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

            const colunas = wrapper
                .find('div')
                .findAll('div')
                .at(1)
                .findAll('div')
            expect(colunas.length).toEqual(5)
            expect(colunas.at(0).html()).toContain('patrimonio de Treinamento 2.0')
            expect(colunas.at(1).html()).toContain('123')
        })

        it('Deve resetar o page para 1 ao mudar a quantidade de itens por página', () => {
            wrapper = shallowMount(PatrimonioListagemGrade, {
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
})
