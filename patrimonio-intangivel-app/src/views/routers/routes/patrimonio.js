import PatrimonioListagem from '@/views/pages/patrimonio/listagem/PatrimonioListagem'
import PatrimonioEdicao from '@/views/pages/patrimonio/edicao/PatrimonioEdicao'
import DadosDeEntradaEdicao from '@/views/pages/patrimonio/edicao/dados-de-entrada/DadosDeEntradaEdicao'
import Documentos from '@/views/pages/patrimonio/edicao/documentos/ListagemDocumentos'
import TipoNovo from '@/views/pages/patrimonio/edicao/tipo/TipoNovo'
import TipoEdicao from '@/views/pages/patrimonio/edicao/tipo/TipoEdicao'
import MenuDetalhe from '@/views/pages/patrimonio/detalhe/MenuDetalhe'
import ModalMovimentacao from '@/views/pages/patrimonio/detalhe/movimentacao/ModalMovimentacao'
import PatrimonioDetalhe from '@/views/pages/patrimonio/detalhe/dadosGerais/PatrimonioDetalhe'
import NovaMovimentacao from '@/views/pages/patrimonio/detalhe/movimentacao/NovaMovimentacao'

export default [
    {
        path: '/patrimonios',
        name: 'TodosPatrimonios',
        component: PatrimonioListagem,
        meta: {
            menu: {
                title: 'Registros',
                icon: 'view_list',
            },
            page: {
                title: 'Registros',
                subtitle: 'Lista geral dos registros de entradas no seu patrimônio'
            },
            requiresAuth: true,
            authorities: ['Patrimonio.Normal', 'Patrimonio.Consulta']
        },
    },
    {
        path: '/patrimonio/novo',
        name: 'PatrimonioNovo',
        component: PatrimonioEdicao,
        redirect: { name: 'TipoNovo' },
        children: [
            {
                path: 'tipo',
                name: 'TipoNovo',
                component: TipoNovo,
                meta: {
                    page: {
                        title: 'Adicionar Nova Entrada',
                        showAutoSave: true
                    },
                    requiresAuth: true,
                    authorities: ['Patrimonio.Normal', 'Patrimonio.Consulta']
                }
            }
        ]
    },
    {
        path: '/patrimonio/:id',
        name: 'PatrimonioEdicao',
        component: PatrimonioEdicao,
        children: [
            {
                path: 'tipo/edicao',
                name: 'TipoEdicao',
                component: TipoEdicao,
                meta: {
                    page: {
                        title: 'Editar Entrada',
                        showAutoSave: true
                    },
                    requiresAuth: true,
                    authorities: ['Patrimonio.Normal', 'Patrimonio.Consulta']
                }
            },
            {
                path: 'dados-de-entrada/edicao',
                name: 'DadosDeEntradaEdicao',
                component: DadosDeEntradaEdicao,
                meta: {
                    page: {
                        title: 'Editar Entrada',
                        showAutoSave: true
                    },
                    requiresAuth: true,
                    authorities: ['Patrimonio.Normal', 'Patrimonio.Consulta']
                }
            },
            {
                path: 'documentos/edicao',
                name: 'DocumentosEdicao',
                component: Documentos,
                meta: {
                    page: {
                        title: 'Editar Entrada',
                        showAutoSave: true
                    },
                    requiresAuth: true,
                    authorities: ['Patrimonio.Normal', 'Patrimonio.Consulta']
                }
            }
        ]
    },
    {
        path: '/apresentacao/:id',
        name: 'MenuDetalhe',
        component: MenuDetalhe,
        children : [
            {
                path: 'movimentacao',
                name: 'NovaMovimentacao',
                component: NovaMovimentacao,
                props: {tabs: 1},
                meta: {
                    page: {
                        title: 'Movimentar Patrimônio'
                    },
                    requiresAuth: true,
                    authorities: ['Patrimonio.Normal']
                },
                children : [
                    {
                        path: 'editar',
                        name: 'ModalMovimentacao',
                        component: ModalMovimentacao,
                        meta: {
                            page: {
                                title: 'Dados Individuais do Patrimônio'
                            },
                            requiresAuth: true,
                            authorities: ['Patrimonio.Normal']
                        }
                    }
                ]
            },
            {
                path: 'ficha',
                name: 'FichaPatrimonio',
                component: PatrimonioDetalhe,
                props: {tabs: 0},
                meta: {
                    page: {
                        title: 'Dados Individuais do Patrimônio'
                    },
                    requiresAuth: true,
                    authorities: ['Patrimonio.Normal']
                }
            }
        ]
    }
]
