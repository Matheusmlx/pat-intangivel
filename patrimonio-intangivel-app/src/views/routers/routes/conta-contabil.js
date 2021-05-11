import Configuracao from '@/views/pages/configuracao/Configuracao'
import ContaContabilListagem from '@/views/pages/configuracao/conta-contabil/listagem/ContaContabilListagem.vue'
import AmortizacaoManual from '@/views/pages/configuracao/amortizacao/AmortizacaoManual'

export default [
    {
        path: '/configuracao',
        name: 'configuracao',
        component: Configuracao,
        meta: {
            menu: {
                title: 'Configurações',
                icon: 'fas fa-cog'
            },
            requiresAuth: true,
            authorities: ['ContaContabil.Normal']
        },
        children: [
            {
                path: '/contas-contabeis',
                name: 'ContaContabilListagem',
                component: ContaContabilListagem,
                meta: {
                    menu: {
                        title: 'Contas Contábeis'
                    },
                    page: {
                        title: 'Contas Contábeis',
                        subtitle: 'Lista geral das contas contábeis'
                    },
                    requiresAuth: true,
                    authorities: ['ContaContabil.Normal']
                }
            },
            {
                path: '/amortizacao-manual',
                name: 'AmortizacaoManual',
                component: AmortizacaoManual,
                meta: {
                    menu: {
                        title: 'Amortização Manual'
                    },
                    page: {
                        title: 'Amortização Manual',
                        subtitle: 'Amortização manual de patrimônios'
                    },
                    requiresAuth: true,
                    authorities: ['ContaContabil.Normal']
                }
            }


        ]
    }
]
