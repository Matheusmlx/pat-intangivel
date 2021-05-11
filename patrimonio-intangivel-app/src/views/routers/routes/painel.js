import Painel from '@/views/pages/dashboard/Painel'

export default [
    {
        path: '/painel',
        name: 'Painel',
        component: Painel,
        meta: {
            menu: {
                title: 'Painel',
                icon: 'fas fa-th-large',
            },
            page: {
                title: 'Painel',
                subtitle: 'Resumo das operações do sistema de Patrimônio Intangível'
            },
            requiresAuth: true,
            authorities: ['Patrimonio.Normal', 'Patrimonio.Consulta']
        },
    }
]
