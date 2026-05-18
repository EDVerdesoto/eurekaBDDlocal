using CLIMOV_EurekaBank_RESTDOTNET_GR03.Services;
using Microsoft.Maui.Controls.Shapes;

namespace CLIMOV_EurekaBank_RESTDOTNET_GR03.Views
{
    public partial class AccountsScreen : ContentPage
    {
        private readonly EurekaBankClient _client;
        private readonly CollectionView _accountsList;
        private readonly ActivityIndicator _loading;
        private readonly Label _messageLabel;

        public AccountsScreen(EurekaBankClient client)
        {
            _client = client;
            Title = "Cuentas";
            BackgroundColor = Color.FromArgb("#F5F5F5");
            NavigationPage.SetHasNavigationBar(this, false);

            _accountsList = new CollectionView { SelectionMode = SelectionMode.None, Margin = new Thickness(0, 8) };
            _accountsList.ItemTemplate = new DataTemplate(() =>
            {
                var codigo = MakeLabel(18, FontAttributes.Bold, "#202124");
                codigo.SetBinding(Label.TextProperty, nameof(AccountSummary.Codigo));
                var cliente = MakeLabel(14, FontAttributes.None, "#4D4D4D");
                cliente.SetBinding(Label.TextProperty, nameof(AccountSummary.ClienteMoneda));
                var saldo = MakeLabel(15, FontAttributes.Bold, "#7C3AED");
                saldo.SetBinding(Label.TextProperty, nameof(AccountSummary.SaldoTexto));
                var estado = MakeLabel(13, FontAttributes.None, "#6F6F6F");
                estado.SetBinding(Label.TextProperty, nameof(AccountSummary.EstadoEmail));

                var grid = new Grid { ColumnDefinitions = { new ColumnDefinition(GridLength.Star), new ColumnDefinition(GridLength.Auto) } };
                grid.Add(new VerticalStackLayout { Spacing = 5, Children = { codigo, cliente, saldo, estado } }, 0);
                grid.Add(new Label { Text = "›", FontSize = 34, TextColor = Color.FromArgb("#777777"), VerticalOptions = LayoutOptions.Center }, 1);
                var tap = new TapGestureRecognizer();
                tap.Tapped += OnAccountTapped;
                grid.GestureRecognizers.Add(tap);

                return new Border
                {
                    BackgroundColor = Colors.White,
                    StrokeThickness = 0,
                    Margin = new Thickness(16, 8),
                    Padding = 16,
                    StrokeShape = new RoundRectangle { CornerRadius = 8 },
                    Content = grid
                };
            });

            _loading = new ActivityIndicator { Color = Color.FromArgb("#7C3AED"), IsRunning = true };
            _messageLabel = new Label { HorizontalTextAlignment = TextAlignment.Center, TextColor = Color.FromArgb("#D32F2F"), Margin = 20 };

            Content = new Grid
            {
                RowDefinitions = { new RowDefinition(64), new RowDefinition(GridLength.Star) },
                Children =
                {
                    Header("Cuentas", OnRefreshClicked),
                    new Grid
                    {
                        Children = { _accountsList, _loading, _messageLabel }
                    }.Row(1)
                }
            };
        }

        protected override async void OnAppearing()
        {
            base.OnAppearing();
            await LoadAccountsAsync();
        }

        private async void OnRefreshClicked(object? sender, EventArgs e) => await LoadAccountsAsync();

        private async Task LoadAccountsAsync()
        {
            _loading.IsVisible = true;
            _loading.IsRunning = true;
            _messageLabel.Text = string.Empty;
            try
            {
                var accounts = await _client.GetAccountsAsync();
                _accountsList.ItemsSource = accounts;
                if (accounts.Count == 0)
                {
                    _messageLabel.Text = "No hay cuentas disponibles.";
                }
            }
            catch (Exception ex)
            {
                _accountsList.ItemsSource = null;
                _messageLabel.Text = $"No se pudieron cargar las cuentas: {ex.Message}";
            }
            finally
            {
                _loading.IsRunning = false;
                _loading.IsVisible = false;
            }
        }

        private async void OnAccountTapped(object? sender, TappedEventArgs e)
        {
            if (sender is BindableObject bindable && bindable.BindingContext is AccountSummary account)
            {
                await Navigation.PushAsync(new MovementsScreen(_client, account.Codigo));
            }
        }

        private static Grid Header(string title, EventHandler refreshHandler)
        {
            var header = new Grid
            {
                BackgroundColor = Color.FromArgb("#7C3AED"),
                Padding = new Thickness(16, 0),
                ColumnDefinitions = { new ColumnDefinition(GridLength.Star), new ColumnDefinition(GridLength.Auto) }
            };
            header.Add(new HorizontalStackLayout
            {
                Spacing = 8,
                VerticalOptions = LayoutOptions.Center,
                HorizontalOptions = LayoutOptions.Center,
                Children =
                {
                    new Image { Source = "sulli_general.png", WidthRequest = 32, HeightRequest = 32 },
                    new Label { Text = title, TextColor = Colors.White, FontSize = 20, FontAttributes = FontAttributes.Bold, VerticalTextAlignment = TextAlignment.Center }
                }
            }, 0);
            var refresh = new Button { Text = "↻", BackgroundColor = Colors.Transparent, TextColor = Colors.White, FontSize = 24, WidthRequest = 52 };
            refresh.Clicked += refreshHandler;
            header.Add(refresh, 1);
            return header;
        }

        private static Label MakeLabel(double size, FontAttributes attributes, string color) => new()
        {
            FontSize = size,
            FontAttributes = attributes,
            TextColor = Color.FromArgb(color)
        };
    }

    internal static class GridExtensions
    {
        public static T Row<T>(this T view, int row) where T : View
        {
            Grid.SetRow(view, row);
            return view;
        }
    }
}
