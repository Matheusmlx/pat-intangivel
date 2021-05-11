import Movimentacao from '@/views/pages/patrimonio/movimentacao/Movimentacao'
import MovimentacaoDetalhe from '@/views/pages/patrimonio/movimentacao/visualizacao/MovimentacaoDetalhe'
import MovimentacaoListagem from '@/views/pages/patrimonio/movimentacao/listagem/MovimentacaoListagem'
import MovimentacaoListagemPendente from '@/views/pages/patrimonio/movimentacao/listagem/MovimentacaoListagemPendente'

export default [
    {
        path: '/movimentacao',
        name: 'TodasMovimentacoes',
        component: Movimentacao,
        meta: {
            menu: {
                title: 'Movimentações',
                icon: 'fas fa-exchange-alt',
            },
            requiresAuth: true,
            authorities: ['Patrimonio.Normal', 'Patrimonio.Consulta']
        },
        children: [
            {
                path: '/movimentacoes-externas/todas',
                name: 'MovimentacaoListagem',
                component: MovimentacaoListagem,
                meta: {
                    menu: {
                        title: 'Externas'
                    },
                    page: {
                        title: 'Movimentações Externas',
                        subtitle: 'Lista geral das movimentações dos patrimônios'
                    },
                    requiresAuth: true,
                    authorities: ['Patrimonio.Normal', 'Patrimonio.Consulta']
                }
            },
            {
                path: '/movimentacoes-externas/receber',
                name: 'MovimentacaoListagemPendente',
                component: MovimentacaoListagemPendente,
                meta: {
                    requiresAuth: true,
                    authorities: ['Patrimonio.Normal', 'Patrimonio.Consulta']
                }
            }
        ]
    },
    {
        path: '/movimentacao/:id/visualizar',
        name: 'MovimentacaoDetalhe',
        component: MovimentacaoDetalhe,
        meta: {
            page: {
                title: 'Visualizar Movimentação'
            },
            requiresAuth: true,
            authorities: ['Patrimonio.Normal', 'Patrimonio.Consulta']
        },
    }
]
